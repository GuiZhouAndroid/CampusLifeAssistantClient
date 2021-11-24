package work.lpssfxy.www.campuslifeassistantclient.base;

import com.tencent.tauth.Tencent;

import work.lpssfxy.www.campuslifeassistantclient.entity.QQUserBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.SessionBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.login.SessionUserBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.login.UserQQSessionBean;

/**
 * created by on 2021/8/23
 * 描述：校园帮后端开发的API接口+常量类
 *
 * @author ZSAndroid
 * @create 2021-08-23-14:20
 */
public class Constant {

    /**
     * 网络请求Gson解析常量类对象
     */
    // QQSession
    public static SessionBean QQSession;
    // QQ个人资料
    public static QQUserBean qqUser;
    // QQSession+用户ID(用于判断账户是否授权QQ，对应MySQL数据库表《t_qq_oauth2_session》)
    public static SessionUserBean sessionUser;
    // QQSession+用户全部信息(并集信息)
    public static UserQQSessionBean userQQSession;
    // QQSession+用户全部信息(并集信息中的QQSession)
    public static UserQQSessionBean.Data userQQSessionData;
    // QQSession+用户全部信息(并集信息中的用户全部信息)
    public static UserQQSessionBean.Data.UserInfo userInfo;

    /**
     * Activity全局唯一请求码
     */
    public static final int REQUEST_CODE_VALUE = 100;//Activity请求码---> startActivityForResult(new Intent(this,xxx.class),REQUEST_CODE_UPDATE);

    /**
     * Activity对应结果码
     */
    public static final int RESULT_CODE_BIND_ACCOUNT_BANNED = 101;//绑定账户被封禁结果码
    public static final int RESULT_CODE_BIND_ACCOUNT_SUCCESS = 102;//绑定账户成功
    public static final int RESULT_CODE_BIND_ACCOUNT_ERROR = 103;//绑定账户失败
    public static final int RESULT_CODE_QQ_ONE_KEY_USERINFO_AND_QQ_SESSION = 104;//QQ会话一键登录 Gson解析的实体对象数据
    public static final int RESULT_CODE_REGISTER_ACCOUNT_SUCCESS = 105;//注册账户成功

    /**
     * 腾讯QQ第三方登录
     */
    // 全局实例对象
    public static Tencent mTencent;
    // QQ互联创建应用的APP_ID
    public static String APP_ID = "101965703";

    /**
     * 服务器+协议+端口号+域名
     */
    // 阿里云服务器后端数据备案域名(含SSL证书)
    public static final String BASE_URL = "https://www.lpssfxy.work";
    // 阿里云对象数据OSS自定义备案域名(含SSL证书)
    private static final String BASE_OSS_URL = "https://www.zsitking.top";
    // 阿里云OSS存储文件夹路径(非目录名)
    private static final String OSS_IMG_PATH = "/pic_data/";

    /**
     * 用户登录注册相关API接口
     */
    // 用户名密码注册
    public static final String REGISTER_USER_INFO = BASE_URL + "/api/user/registerUserInfo";

    // 用户名密码登录
    public static final String LOGIN_USERNAME_PASSWORD = BASE_URL + "/api/user/userDoLoginByNameAndPwd";

    // 用户名密码登录，拉去并集信息
    public static final String LOGIN_USERNAME_PASSWORD_AND_QQ_SESSION = BASE_URL + "/api/user/userSelectUserInfoAndQQInfoByUserNameAndPasswordToLogin";

    // 用户名密码登录，参数一：ulTel手机号码，参数二：ulPassword登录密码
    public static final String LOGIN_TEL_PASSWORD = BASE_URL + "/api/user/userDoLoginByTelAndPwd";

    // 手机号快捷登录
    public static final String LOGIN_FAST_TEL = BASE_URL + "/api/user/userSelectUserInfoAndQQInfoByUserTelToLogin";

    // 查询QQ是否授权登录过校园帮APP
    public static final String LOGIN_QQ_SESSION = BASE_URL + "/api/qq/oauth/session/selectIfBindQQSession";

    // 通过用户自增ID添加会话
    public static final String LOGIN_ADD_QQ_SESSION = BASE_URL + "/api/qq/oauth/session/addQQSessionInfoByUserId";

    // 通过用户自增ID查询QQ会话和用户全部信息
    public static final String LOGIN_SELECT_QQ_AND_USER_INFO = BASE_URL + "/api/qq/oauth/session/selectUserInfoAndQQInfoByUserId";

    // 通过用户自增ID更新QQ会话全部信息
    public static final String LOGIN_UPDATE_QQ_SESSION_ALL_INFO_BY_USERID = BASE_URL + "/api/qq/oauth/session/updateQQSessionInfoByUserIdToAll";

    // 查询某用户与QQ登录会话信息
    public static final String FIND_USER_AND_QQ_INFO_BY_USERID = BASE_URL + "/api/user/findUserAndQQInfoByUserId";

    /**
     * 用户个人信息管理相关API接口
     */
    // 通过用户ID查询个人全部信息
    public static final String SELECT_USER_ALL_INFO_BY_USERID = BASE_URL + "/api/user/selectUserInfoByUserId";

    // 更新用户名
    public static final String UPDATE_USERNAME = BASE_URL + "/api/user/updateOldUserNameByNewUserNameAndUserName";

    // 更新密码
    public static final String UPDATE_PASSWORD = BASE_URL + "/api/user/updateOldPasswordByNewPasswordAndUserName";

    // 更新性别
    public static final String UPDATE_SEX = BASE_URL + "/api/user/updateOldSexByNewSexAndUserName";

    // 更新真实姓名
    public static final String UPDATE_REAL_NAME = BASE_URL + "/api/user/updateOldRealNameByNewRealNameAndUserName";

    // 更新身份证号
    public static final String UPDATE_ID_CARD = BASE_URL + "/api/user/updateOldIdCardByNewIdCardAndUserName";

    // 更新学号
    public static final String UPDATE_STU_NO = BASE_URL + "/api/user/updateOldStuNoByNewStuNoAndUserName";

    // 更新手机号
    public static final String UPDATE_TEL = BASE_URL + "/api/user/updateOldTelByNewTelAndUserName";

    // 更新QQ邮箱
    public static final String UPDATE_QQ_EMAIL = BASE_URL + "/api/user/updateOldEmailNewEmailAndUserName";

    // 更新所属院系
    public static final String UPDATE_DEPT = BASE_URL + "/api/user/updateOldDeptNewDeptAndUserName";

    // 更新专业班级
    public static final String UPDATE_CLASS = BASE_URL + "/api/user/updateOldClassNewClassAndUserName";

    // 当前登录会话的用户名，查询该用户的对应的角色集合
    public static final String SELECT_NOW_USERNAME_ROLE_LIST = BASE_URL + "/api/user/selectRoleListByRedisToUserName";

    /**
     * 检测用户登录注销状态相关API接口
     */
    // 查询当前已登录会话状态
    public static final String SA_TOKEN_IS_LOGIN = BASE_URL + "/api/login/state/saTokenIsLogin";

    // 检查当前是否登录
    public static final String SA_TOKEN_CHECK_LOGIN = BASE_URL + "/api/login/state/saTokenCheckLogin";

    // 注销当前已登录账号
    public static final String SA_TOKEN_DO_LOGOUT = BASE_URL + "/api/login/state/saTokenDoLogout";

    // 通过账户名查询封禁信息
    public static final String QUERY_BANNED_STATE_BY_USERNAME = BASE_URL + "/api/user/userSelectBannedAccountInfoByUsername";

    // 通过账户ID查询封禁信息
    public static final String QUERY_BANNED_STATE_BY_USERID = BASE_URL + "/api/user/userSelectBannedAccountInfoByUserId";

    // 通过账户ID查询封禁信息+查询QQ会话和用户全部信息
    public static final String QUERY_BANNED_STATE_BY_USERID_AND_ONE_KEY_QQ_LOGIN = BASE_URL + "/api/user/userSelectUserInfoAndQQInfoByUserIdToOneKeyQQLoginAndUpdateQQSession";

    /**
     * 用户登录会话查询相关API接口
     */
    // 查询当前已登录账户Token值
    public static final String SA_TOKEN_GET_TOKEN_VALUE = BASE_URL + "/api/login/state/saTokenGetTokenValue";

    // 查询Sa-Token名称
    public static final String SA_TOKEN_GET_TOKEN_NAME = BASE_URL + "/api/login/state/saTokenGetTokenName";

    // 查询当前已登录账户Token参数信息
    public static final String SA_TOKEN_GET_TOKEN_PARAM_INFO = BASE_URL + "/api/login/state/saTokenGetTokenParamInfo";

    // 查询当前已登录会话ID值
    public static final String SA_TOKEN_GET_LOGIN_ID_VALUES = BASE_URL + "/api/login/state/saTokenGetLoginIdValues";

    /**
     * User-Session相关API接口
     */
    // 查询当前已登录账号ID的Session信息
    public static final String SA_TOKEN_GET_SESSION_INFO = BASE_URL + "/api/login/state/saTokenGetSessionInfo";

    // 查询当前已登录账号ID的Session尚未创建时是否新建并返回
    public static final String SA_TOKEN_GET_SESSION_INFO_TRUE = BASE_URL + "/api/login/state/saTokenGetSessionInfoTrue";

    // 此接口待处理
    public static final String SA_TOKEN_GET_SESSION_LOGIN_ID = BASE_URL + "/api/login/state/getSessionByLoginId";

    /**
     * Token-Session----->自定义Session(本系统暂不考虑)
     */
    // 查询当前Token值的专属Session
    public static final String SA_TOKEN_GET_TOKEN_SESSION = BASE_URL + "/api/login/state/saTokenGetTokenSession";

    // 查询指定Token值的专属Session
    public static final String SA_TOKEN_GET_TOKEN_SESSION_BY_TOKEN = BASE_URL + "/api/login/state/saTokenGetTokenSessionByToken";

    /**
     * 超管后台中心相关API接口
     */
    // 超管通过已登录账户真实姓名踢人下线
    public static final String ADMIN_KICK_BY_REAL_NAME = BASE_URL + "/api/admin/kickOffLine/adminToKickOffLineByLoginRealName";

    // 超管通过已登录账户Token令牌值踢人下线
    public static final String ADMIN_KICK_BY_TOKEN_VALUES = BASE_URL + "/api/admin/kickOffLine/adminToKickOffLineByLoginTokenValues";

    // 超管通过已登录账户真实姓名封禁账户
    public static final String ADMIN_BANNED_ACCOUNT_BY_REAL_NAME = BASE_URL + "/api/admin/kickOffLine/adminToBannedAccountByRealName";

    // 超管通过已登录账户真实姓名查询封禁状态
    public static final String ADMIN_SELECT_BANNED_ACCOUNT_STATE_BY_REAL_NAME = BASE_URL + "/api/admin/kickOffLine/adminSelectBannedAccountStateByRealName";

    // 超管通过已登录账户真实姓名查询剩余封禁时间
    public static final String ADMIN_SELECT_BANNED_ACCOUNT_RESIDUE_TIME_BY_REAL_NAME = BASE_URL + "/api/admin/kickOffLine/adminSelectBannedAccountResidueTimeByRealName";

    // 超管通过已登录账户真实姓名解除封禁
    public static final String ADMIN_TO_UNTIE_BANNED_ACCOUNT_BY_REAL_NAME = BASE_URL + "/api/admin/kickOffLine/adminToUntieBannedAccountByRealName";

    // 超管添加一条角色信息
    public static final String ADMIN_ADD_ONCE_ROLE_INFO = BASE_URL + "/api/admin/role/adminAddOnceRoleInfo";

    // 超管查询角色全部信息
    public static final String ADMIN_SELECT_ALL_ROLE_INFO = BASE_URL + "/api/admin/role/adminSelectAllRoleInfo";

}

