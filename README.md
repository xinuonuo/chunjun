```mermaid
graph TD;
    subgraph 用户界面
        A[用户注册模块]
        B[用户登录模块]
        C[健康数据输入模块]
        D[健康报告展示模块]
        E[通知中心模块]
    end

    subgraph API 控制器
        F[用户管理模块]
        G[健康数据管理模块]
        H[报告生成模块]
        I[通知管理模块]
    end

    subgraph 健康管理服务
        J[数据分析模块]
        K[报告生成模块]
        L[建议生成模块]
        M[风险评估模块]
    end

    subgraph 数据存储
        N[用户数据存储]
        O[健康数据存储]
        P[报告存储]
    end

    subgraph 通知服务
        Q[邮件通知模块]
        R[推送通知模块]
        S[消息队列模块]
    end

    subgraph 可穿戴设备接口
        T[数据接收模块]
        U[数据处理模块]
    end

    subgraph 医疗服务提供者接口
        V[医生登录模块]
        W[用户健康数据查看模块]
        X[在线咨询模块]
    end

    A -->|使用| F
    B -->|使用| F
    C -->|使用| G
    D -->|使用| H
    E -->|使用| I

    F -->|调用| J
    G -->|调用| K
    H -->|调用| L
    I -->|调用| Q

    J -->|存取| N
    K -->|存取| O
    L -->|存取| P

    J -->|通知| Q
    L -->|通知| R
    S -->|接收| T

    T -->|同步| J
    U -->|处理| J

    V -->|查看| N
    W -->|查看| O
    X -->|咨询| I
