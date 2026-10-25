package com.example.experience.infrastructure.sync.adapter.builtin;

import java.net.HttpCookie;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.experience.common.exception.SyncAPIAuthException;
import com.example.experience.common.exception.SyncAPIClientException;
import com.example.experience.common.exception.SyncAPIServiceException;
import com.example.experience.infrastructure.sync.adapter.AdapterManifest;
import com.example.experience.infrastructure.sync.adapter.FetchContext;
import com.example.experience.infrastructure.sync.adapter.SyncAdapterHandler;
import com.example.experience.infrastructure.sync.adapter.auth.AuthInitRequest;
import com.example.experience.infrastructure.sync.adapter.auth.AuthInitResponse;
import com.example.experience.infrastructure.sync.adapter.builtin.BiliBiliAdapter.BiliFavFolder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kotlin.OverloadResolutionByLambdaReturnType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BiliBiliAdapter implements SyncAdapterHandler {

    @Override
    public AdapterManifest manifest(){
        return AdapterManifest.builder().version("1").build(); 
    }

    private final RestClient biliClient = RestClient.builder()
            .defaultStatusHandler(HttpStatusCode::is5xxServerError, (req, res) -> {
                throw new SyncAPIServiceException("BiliBili API Service Error: " + res.getStatusCode());
            })
            .defaultStatusHandler(status -> status.value() == 401 || status.value() == 403, (req, res) -> {
                throw new SyncAPIAuthException("BiliBili API Auth Error: " + res.getStatusCode());
            })
            .defaultStatusHandler(HttpStatusCode::is4xxClientError, (req, res) -> {
                throw new SyncAPIClientException("BiliBili API Client Error: " + res.getStatusCode());
            })
            .build();

    private final ObjectMapper objectMapper;

    public BiliBiliAdapter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void  fetchEvents(FetchContext ctx) {
    }

    @Override
    public void  uploadEvent(FetchContext ctx) {
    }


    private BiliDTO.LoginPageUrlAndQrcodeKeyResponse getLoginPageUrlAndQrcodeKey() {
        URI uri = UriComponentsBuilder.fromUriString(BiliHost.PASSPORT)
                .path(BiliUri.GETLOGINQRCODEURI)
                .build()
                .toUri();
        JsonNode response = biliClient.get()
                .uri(uri)
                .retrieve()
                .body(JsonNode.class);
        String responseUrl = response.get("data").get("url").asText();
        String responseQrcodeKey = response.get("data").get("qrcode_key").asText();
        return new BiliDTO.LoginPageUrlAndQrcodeKeyResponse(responseUrl, responseQrcodeKey);
    }

    private BiliDTO.LoginResultResponse getLoginResult(BiliDTO.LoginResultRequest request) {
        URI uri = UriComponentsBuilder.fromUriString(BiliHost.PASSPORT)
                .path(BiliUri.GETLOGINRESULTURI)
                .queryParam("qrcode_key", request.QrcodeKey)
                .build()
                .toUri();
        ResponseEntity<JsonNode> response = biliClient.get()
                .uri(uri)
                .retrieve()
                .toEntity(JsonNode.class);
        JsonNode body = response.getBody();
        int code = body.get("data").get("code").asInt();
        if (code != 0) {
            throw new SyncAPIClientException("BiliBili API Client Error: " + code);
        }
        HttpHeaders headers = response.getHeaders();
        List<String> setCookieHeaders = headers.getOrEmpty(HttpHeaders.SET_COOKIE);
        List<BiliCookie> cookies = setCookieHeaders.stream()
                .flatMap(header -> HttpCookie.parse(header).stream())
                .map(cookie -> new BiliCookie(
                        cookie.getName(),
                        cookie.getValue(),
                        cookie.getDomain(),
                        cookie.getPath(),
                        expiresFromMaxAge(cookie.getMaxAge()),
                        cookie.getSecure(),
                        cookie.isHttpOnly()
                ))
                .toList();

        if (cookies.stream().noneMatch(c -> "SESSDATA".equals(c.name()))) {
            throw new SyncAPIClientException("BiliBili login response missing SESSDATA cookie");
        }

        String url = body.get("url").asText();
        String refreshToken = body.get("refresh_token").asText();
        Long timestampV = body.get("timestamp").asLong();
        Instant timestamp = Instant.ofEpochMilli(timestampV);
        Instant expiresAt = cookies.stream()
                .map(BiliCookie::expires)
                .filter(exp -> exp != null)
                .min(Instant::compareTo)
                .orElse(null);

        BiliCookieCredential credential = new BiliCookieCredential(cookies, refreshToken, expiresAt);
        CredentialEnvelope<BiliCookieCredential> envelope = new CredentialEnvelope<>(
                1, "bilibili:cookie", Instant.now(), credential);

        String credentialJson;
        try {
            credentialJson = objectMapper.writeValueAsString(envelope);
        } catch (Exception e) {
            throw new SyncAPIClientException("Failed to serialize BiliBili credential: " + e.getMessage());
        }

        return new BiliDTO.LoginResultResponse(url, refreshToken, timestamp, credentialJson);
    }

    private Instant expiresFromMaxAge(long maxAge) {
        return maxAge < 0 ? null : Instant.now().plusSeconds(maxAge);
    }

    private String toCookieHeader(List<BiliCookie> cookies) {
        return cookies.stream()
                .map(c -> c.name() + "=" + c.value())
                .collect(Collectors.joining("; "));
    }

    private static final class BiliUri {

        public static final String GETLOGINQRCODEURI = "/x/passport-login/web/qrcode/generate";
        public static final String GETLOGINRESULTURI = "/x/passport-login/web/qrcode/poll";
        public static final String GETIFCOOKIENEEDREFRESHURI = "/x/passport-login/web/cookie/info";
        public static final String GETMYINFOURI = "/x/member/web/account";
        public static final String GETFAVFOLDERINFOURI = "/x/v3/fav/folder/info";
        public static final String GETUSERFAVFOLDERLISTURI = "/x/v3/fav/folder/created/list-all";
        public static final String GETUSERFAVVEDIOFOLDERLISTURI = "/x/v3/fav/folder/collected/list";
    }

    private static final class BiliHost {

        public static final String PASSPORT = "https://passport.bilibili.com";
        public static final String API = "https://api.bilibili.com";
    }

    public static record CredentialEnvelope<T>(int version, String type, Instant createdAt, T payload) {}

    public static record BiliCookieCredential(List<BiliCookie> cookies, String refreshToken, Instant expiresAt) {}

    public static record BiliCookie(
            String name,
            String value,
            String domain,
            String path,
            Instant expires,
            boolean secure,
            boolean httpOnly
            ) {}

    public static record BiliVideo(
            String bvid,//bv
            long aid,//av
            String url,
            boolean isLike,
            boolean isFav,
            boolean isCoin,
            BiliFavFolder favFolder
            ) {}

    public static record BiliFavFolder(
            long id,//收藏夹id(media_id)
            long fid,
            long creatbyId,//创建者mid
            int attr,//收藏夹属性位表0位标识是否为私有0公开1私有,1位表示是否为默认0默认1其他
            String title,
            int mediaCount,
            Instant ctime,
            Instant mtime,
            boolean fav_state,
            boolean like_state
            ) {}

    public static record BiliUp(
            long id,//up主mid
            String spaceUrl,//uri
            boolean isSubscribe,//是否订阅
            String name,//昵称
            String sex,//性别男/女/保密
            String face,//头像uri
            String sign,//签名
            int rank,//用户权限等级
            int level,//等级
            //Instant joinTime,//入站事件//文档显示恒为0,测试符合文档不计入dto
            boolean isSilence,//封禁状态
            //int coins,//硬币数实验证明返回非自己的info时为0
            int moral,//节操值//文档中显示这一项恒为0,需测试
            boolean haveFansBadge,//是否有粉丝勋章
            boolean wearingFansBadge,//是否佩戴粉丝勋章
            boolean showingFansBadge,//是否显示粉丝勋章
            List<FanMedal> fanMedals,//粉丝勋章列表
            String topPhoto,//主页头图uri 
            Instant birthday,//生日
            boolean isSeniorMember,//是否为大会员
            String sysNotice,//系统通知,本来不想加进来的发现这个会提示纪念账号,有所触动,故添加,本身是object现只记录通知内容
            String school,//学校本来是个对象可是对象中只有一个name字段故直接记录name
            BiliUserOfficial official,//认证信息
            BiliUserProfession profession,//职业信息
            boolean is100Honour,//百大
            int Followings,//关注数
            int Followers,//粉丝数
            long likeCount,//获赞数
            int archiveCount,//投稿数
            int videoCount,//视频数
            int articleCount,//专栏数
            int blackCount,//拉黑数
            int whisperCount,//悄悄关注数
            long archiveView,//总播放数
            long articleView,//专栏总阅读数
            long bangumiCount,//追番数
            long cinemaCount,//追剧数
            long channelCount,//视频列表数
            long favouriteCount,//收藏夹数
            long tagCount,//关注tag数
            long seasonCount,//视频合集数
            long uposCount,//动态数
            long pugvCount,//课程数
            long audioCount,//音频数
            long albumCount//图文数
            ) {}

    private static record BiliUserOfficial(
            int role,//见https://sessionhu.github.io/bilibili-API-collect/docs/user/official_role.html
            String title,//认证信息
            String desc,//认证备注
            int type//认证类型-1：无0：个人认证1：机构认证
            ) {}

    private static record BiliUserProfession(
            String name,//资质名称
            String showName,
            String department,//职位
            String title,//所属机构
            int isShow,//是否显示0n1y
            String realname,
            String certificateNo,
            boolean certificateShow//这两个是接口的不知道用处
            ) {}

    private static record BiliLiveRoom() {}

    private static record FanMedal(
            long uid,//拥有粉丝牌用户id
            long targetId,//粉丝牌对应up主id
            long medalId,//粉丝牌id
            int level,//粉丝等级
            String medalName,//粉丝牌名称
            int medalColor,
            int intimacy,//亲密度
            int nextIntimacy,//到达下一级亲密度
            int dayLimit,//每日亲密度上限
            int todayFeed,//今日亲密度
            int medalColorStart,//粉丝牌颜色起始值
            int medal_color_border,//粉丝牌颜色边框值
            int isLigheted,//是否点亮过
            int LightStatus,//点亮状态
            int score//滚木
            ) {}

    private static record BiliMe(
            long mid,//用户id
            String name,//昵称
            String sex,//性别
            String face,//头像uri
            String sign,//签名
            Instant birthday,//生日
            int rank,//用户权限等级
            int level,//等级
            int moral,//节操值
            int isSilence,//封禁状态
            int emailStatus,//邮箱验证状态
            int telStatus,//手机号验证状态
            int isTourist,//作用不明确
            int isFakeAccount,//作用不明确
            int pinPrompting,//作用不明确
            int isDeteted,//作用不明确
            double coins,//硬币数
            double following,//关注数
            double follower,//粉丝数
            int vipType,//大会员类型
            int vipStatus,//大会员状态
            int vipDueDate,//大会员到期时间
            //以上三个字段均为vip对象中的字段我懒得再写一个vip的record了
            long pid,//挂件id
            String pidName,//挂件名称
            String pidIcon,//挂件图标uri
            String pidImageEnhance,//如果挂件是动图其动图uri
            //以上三个是pendant对象中的字段
            BiliUserOfficial official,//认证信息
            BiliUserProfession profession,//职业信息
            int currentExp,//当前经验
            long likeCount,//获赞数
            int archiveCount,//投稿数
            int videoCount,//视频数
            int articleCount,//专栏数
            int blackCount,//拉黑数
            int whisperCount,//悄悄关注数
            long archiveView,//总播放数
            long articleView,//专栏总阅读数
            long bangumiCount,//追番数
            long cinemaCount,//追剧数
            long channelCount,//视频列表数
            long favouriteCount,//收藏夹数
            long tagCount,//关注tag数
            long seasonCount,//视频合集数
            long uposCount,//动态数
            long pugvCount,//课程数
            long audioCount,//音频数
            long albumCount, //图文数
            BiliFollow follow,//关注信息
            BiliBlock block//拉黑信息
            ) {}
    
    /* 目前只认为存储我的关注 */
    private static record BiliRelation(
        long mid,//用户id
        long targetId,//目标用户id
        int attribute,//关系属性0:无1:关注2:粉丝3:互相关注4:拉黑5:悄悄关注6:被悄悄关注7:被拉黑
        List<Integer> tags,
        boolean isSpecial,//是否为特别关注
        Instant mtime//关注时间
    ){}

    private static record BiliFollowGroup(
        long id,//分组id
        String name,//分组名称
        int count,//分组内关注数
        String tip//提示信息
    ){}

    private static record BiliFollow(
        List<BiliRelation> relations,
        List<BiliFollowGroup> groups,
        int total//总数
    ){}

    private static record BiliBlock(
        int total,//总数
        List<BiliRelation> relations
    ){}

    private static class BiliDTO {

        public record LoginPageUrlAndQrcodeKeyResponse(String responseUrl, String responseQrcodeKey) implements AuthInitResponse {}

        public record LoginResultRequest(String QrcodeKey) {}

        public record LoginResultResponse(String url, String refreshToken, Instant timestamp, String credentialJson) {}

        public record MyInfoGetRequest() {}

        public record MyInfoGetResponse(long mid, String uname, String userId, String sign, Instant birthday, String sex, String rank) {}

        public record GetFavlistRequest(long upMid) {}//upMid:up主mid

        public record GetFavlistResponse(int count, List<BiliFavFolder> folders) {}
    }

}
