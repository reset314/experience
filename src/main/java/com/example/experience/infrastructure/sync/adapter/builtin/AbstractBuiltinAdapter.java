package com.example.experience.infrastructure.sync.adapter.builtin;

import com.example.experience.infrastructure.sync.adapter.SyncAdapterHandler;

/**
 * 内置适配器可选公共基类。
 *
 * <p>内置（随应用打包发布的）同步适配器可以选择继承本类以复用公共逻辑；
 * 也可以直接实现 {@link SyncAdapterHandler}，本基类并非强制要求。</p>
 *
 * <p>公共内置适配器逻辑可放在这里。</p>
 */
public abstract class AbstractBuiltinAdapter implements SyncAdapterHandler {
    // 公共内置适配器逻辑可放在这里
}
