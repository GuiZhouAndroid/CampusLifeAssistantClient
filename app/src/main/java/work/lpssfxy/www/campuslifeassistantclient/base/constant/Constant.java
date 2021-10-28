package work.lpssfxy.www.campuslifeassistantclient.base.constant;

import com.tencent.tauth.Tencent;

import work.lpssfxy.www.campuslifeassistantclient.entity.QQUserBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.QQUserSessionBean;

/**
 * created by on 2021/8/23
 * 描述：校园帮后端开发的API接口
 *
 * @author ZSAndroid
 * @create 2021-08-23-14:20
 */
public class Constant {

    public static final int REQUEST_CODE_VALUE = 100;//Activity请求码---> startActivityForResult(new Intent(this,xxx.class),REQUEST_CODE_UPDATE);
    public static QQUserSessionBean qqUserSessionBean;
    public static QQUserBean qqUser;
    /**
     * 腾讯QQ全局实例对象
     */
    public static Tencent mTencent;
    /**
     * 腾讯QQ APP_ID
     */
    public static String APP_ID = "101965703";

    /**
     * 服务器+协议+端口号+域名
     */
    public static final String BASE_URL = "https://www.lpssfxy.work";

    /**
     * 用户注册
     */
    //用户名密码登录
    public static final String REGISTER_USER_INFO = BASE_URL + "/api/user/registerUserInfo";

    /**
     * 用户登录接口
     */
    //用户名密码登录
    public static final String LOGIN_USERNAME_PASSWORD = BASE_URL + "/api/user/userDoLoginByNameAndPwd";

    //用户名密码登录，参数一：ulTel手机号码，参数二：ulPassword登录密码
    public static final String LOGIN_TEL_PASSWORD = BASE_URL + "/api/user/userDoLoginByTelAndPwd";

    //查询QQ是否授权登录过校园帮APP
    public static final String LOGIN_QQ_SESSION = BASE_URL + "/api/qq/oauth/session/selectIfBindQQSession";

    //通过用户自增ID添加会话
    public static final String LOGIN_ADD_QQ_SESSION = BASE_URL + "/api/qq/oauth/session/addQQSessionInfoByUserId";

    //通过用户自增ID查询QQ会话和用户全部信息
    public static final String LOGIN_SELECT_QQ_AND_USER_INFO = BASE_URL + "/api/qq/oauth/session/selectUserAndQQInfoByUserId";
    /**
     * 检测用户登录状态+注销接口
     */
    //查询当前已登录会话状态
    public static final String SA_TOKEN_IS_LOGIN = BASE_URL + "/api/login/state/saTokenIsLogin";

    //检查当前是否登录
    public static final String SA_TOKEN_CHECK_LOGIN = BASE_URL + "/api/login/state/saTokenCheckLogin";

    //注销当前已登录账号
    public static final String SA_TOKEN_DO_LOGOUT = BASE_URL + "/api/login/state/saTokenDoLogout";

    /**
     * 用户登录会话查询
     */
    //查询当前已登录账户Token值
    public static final String SA_TOKEN_GET_TOKEN_VALUE = BASE_URL + "/api/login/state/saTokenGetTokenValue";

    //查询Sa-Token名称
    public static final String SA_TOKEN_GET_TOKEN_NAME = BASE_URL + "/api/login/state/saTokenGetTokenName";

    //查询当前已登录账户Token参数信息
    public static final String SA_TOKEN_GET_TOKEN_PARAM_INFO = BASE_URL + "/api/login/state/saTokenGetTokenParamInfo";

    //查询当前已登录会话ID值
    public static final String SA_TOKEN_GET_LOGIN_ID_VALUES = BASE_URL + "/api/login/state/saTokenGetLoginIdValues";

    /**
     * User-Session
     */
    //查询当前已登录账号ID的Session信息
    public static final String SA_TOKEN_GET_SESSION_INFO = BASE_URL + "/api/login/state/saTokenGetSessionInfo";

    //查询当前已登录账号ID的Session尚未创建时是否新建并返回
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
     * 管理员权利的账户封禁+踢人下线
     */
    // 管理员通过已登录账户用户名踢人下线
    public static final String ADMIN_KICK_BY_USERNAME = BASE_URL + "/api/admin/kickOffLine/adminToKickOffLineByLoginUsername";

    // 管理员通过已登录账户Token令牌值踢人下线
    public static final String ADMIN_KICK_BY_TOKEN_VALUES = BASE_URL + "/api/admin/kickOffLine/adminToKickOffLineByLoginTokenValues";

    // 管理员通过已登录账户用户名封禁账户
    public static final String ADMIN_BANNED_ACCOUNT_BY_USERNAME = BASE_URL + "/api/admin/kickOffLine/adminToBannedAccountByUsername";

    // 管理员通过已登录账户用户名查询封禁状态
    public static final String ADMIN_SELECT_BANNED_ACCOUNT_STATE_BY_USERNAME = BASE_URL + "/api/admin/kickOffLine/adminSelectBannedAccountStateByUsername";

    // 管理员通过已登录账户用户名查询剩余封禁时间
    public static final String ADMIN_SELECT_BANNED_ACCOUNT_RESIDUE_TIME_BY_USERNAME = BASE_URL + "/api/admin/kickOffLine/adminSelectBannedAccountResidueTimeByUsername";

    // 管理员通过已登录账户用户名解除封禁
    public static final String ADMIN_TO_UNTIE_BANNED_ACCOUNT_BY_USERNAME = BASE_URL + "/api/admin/kickOffLine/adminToUntieBannedAccountByUsername";

    /**
     * 普通用户信息关联QQ登录信息
     */
    // 查询某普通用户与QQ登录会话信息
    public static final String FIND_USER_AND_QQ_INFO_BY_USERID = BASE_URL + "/api/user/findUserAndQQInfoByUserId";
}

