public record UserProfileResponse(
    String userId,
    String displayName,
    boolean isIdVerified,
    String avatarData
) {

}
