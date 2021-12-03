package work.lpssfxy.www.campuslifeassistantclient.base;

import com.tencent.tauth.Tencent;

import work.lpssfxy.www.campuslifeassistantclient.entity.dto.OnlyQQSessionInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.dto.OnlyQQUserInfoBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoSessionAndUserBean;
import work.lpssfxy.www.campuslifeassistantclient.entity.okgo.OkGoUserInTheSessionBean;

/**
 * created by on 2021/8/23
 * 描述：校园帮后端开发的API接口+常量类
 *
 * @author ZSAndroid
 * @create 2021-08-23-14:20
 */
public class Constant {

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
    public static final int RESULT_CODE_OCR_BIND_ACCOUNT_SUCCESS = 106;//实名认证绑定账户成功

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

    /**
     * 用户个人信息管理相关API接口
     */
    // 通过用户ID查询个人全部信息
    public static final String SELECT_USER_ALL_INFO_BY_USERID = BASE_URL + "/api/user/selectUserInfoByUserId";

    // 更新用户名
    public static final String UPDATE_USERNAME = BASE_URL + "/api/user/updateOldUserNameByNewUserNameAndUserName";

    // 更新密码(待使用)
    public static final String UPDATE_PASSWORD = BASE_URL + "/api/user/updateOldPasswordByNewPasswordAndUserName";

    // 更新性别(未使用)
    public static final String UPDATE_SEX = BASE_URL + "/api/user/updateOldSexByNewSexAndUserName";

    // 更新真实姓名(未使用)
    public static final String UPDATE_REAL_NAME = BASE_URL + "/api/user/updateOldRealNameByNewRealNameAndUserName";

    // 更新身份证号(未使用)
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

    // 当前登录账户通过Sa-Token的redis缓存UserSession中的真实姓名(再MP查询用户名)对应的角色信息
    public static final String SA_TOKEN_REDIS_USER_SESSION_SELECT_ROLE_LIST_BY_REAL_NAME_TO_USERNAME = BASE_URL + "/api/user/saTokenRedisUserSessionSelectOnlyUserToRoleListInfoByUserName";

    // 当前登录账户通过Sa-Token的redis缓存UserSession中的真实姓名(再MP查询用户名)对应的权限信息
    public static final String SA_TOKEN_REDIS_USER_SESSION_SELECT_PERMISSION_LIST_BY_REAL_NAME_TO_USERNAME = BASE_URL + "/api/user/saTokenRedisUserSessionSelectOnlyUserToPermissionListInfoByUserName";

    /**
     * 用户个人信息实名认证相关API接口
     */
    // 通过用户ID+真实姓名+性别查询对应用户+实名并集信息+进行实名认证
    public static final String SELECT_CER_ALL_INFO_BY_OCR_DATA_IF_DO_CER = BASE_URL + "/api/user/certification/userSelectUserCerAllInfoByUserIdAndRealNameAndSex";

    // 通过真实姓名查询对应用户实名状态
    public static final String SELECT_CER_STATE_BY_USER_REAL_NAME = BASE_URL + "/api/user/certification/userSelectUserCerStateByUserRealName";

    // 通过身份证号查询对应用户实名状态
    public static final String SELECT_CER_STATE_BY_USER_ID_CARD = BASE_URL + "/api/user/certification/userSelectUserCerStateByUserIdCard";

    // 通过Sa-Token的UserSession查询当前用户实名状态
    public static final String SELECT_NOW_CER_STATE_BY_SA_TOKEN_LOGIN_REAL_NAME = BASE_URL + "/api/user/certification/userSelectNowUserCerState";

    // 通过Sa-Token的UserSession查询当前用户+实名并集信息
    public static final String SELECT_NOW_CER_ALL_INFO_BY_SA_TOKEN_LOGIN_REAL_NAME = BASE_URL + "/api/user/certification/userSelectNowUserCerAllInfoByUserRealName";

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
     * 超管后台中心
     */
    /* ************************ 1.账户封禁相关API接口 *************************/

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

    /* ************************ 2.用户相关API接口 *************************/

    // 超管查询用户表全部信息
    public static final String ADMIN_SELECT_ALL_USER_INFO = BASE_URL + "/api/user/adminSelectAllUserInfo";

    // 超管分页查询用户表信息
    public static final String ADMIN_SELECT_ALL_USER_INFO_BY_PAGE = BASE_URL + "/api/user/adminSelectUserInfoByPage";

    // 超管通过用户ID查询用户信息
    public static final String ADMIN_SELECT_USER_INFO_BY_USER_ID = BASE_URL + "/api/user/adminSelectUserInfoByUserId";

    // 超管通过用户名查询用户信息
    public static final String ADMIN_SELECT_USER_INFO_BY_USERNAME = BASE_URL + "/api/user/adminSelectUserInfoByUserName";

    // 超管通过真实姓名查询用户信息
    public static final String ADMIN_SELECT_USER_INFO_BY_USER_REAL_NAME = BASE_URL + "/api/user/adminSelectUserInfoByRealName";

    /* ************************ 3.角色相关API接口 *************************/

    // 超管添加一条角色信息
    public static final String ADMIN_ADD_ONCE_ROLE_INFO = BASE_URL + "/api/admin/role/adminAddOnceRoleInfo";

    // 超管通过角色ID和旧角色名称更新角色名称
    public static final String ADMIN_UPDATE_ROLE_NAME_BY_ROLE_ID_AND_OLD_ROLE_NAME = BASE_URL + "/api/admin/role/adminUpdateRoleNameByRoleIdAndOldRoleName";

    // 超管通过角色ID和旧角色描述更新角色描述
    public static final String ADMIN_UPDATE_ROLE_DESCRIPTION_BY_ROLE_ID_AND_OLD_ROLE_DESCRIPTION = BASE_URL + "/api/admin/role/adminUpdateRoleDescriptionByRoleIdAndOldRoleDescription";

    // 超管通过角色实体删除一条对应角色信息
    public static final String ADMIN_DELETE_ONCE_ROLE_INFO_BY_ROLE_ENTITY = BASE_URL + "/api/admin/role/adminDeleteOnceRoleInfoByRoleEntity";

    // 超管查询角色全部信息
    public static final String ADMIN_SELECT_ALL_ROLE_INFO = BASE_URL + "/api/admin/role/adminSelectAllRoleInfo";

    // 超管通过角色ID查询角色信息
    public static final String ADMIN_SELECT_ROLE_INFO_BY_ROLE_ID = BASE_URL + "/api/admin/role/adminSelectRoleInfoByRoleId";

    // 超管通过角色名称查询角色信息
    public static final String ADMIN_SELECT_ROLE_INFO_BY_ROLE_NAME = BASE_URL + "/api/admin/role/adminSelectRoleInfoByRoleName";

    /* ************************ 4.权限相关API接口 *************************/

    // 超管添加一条权限信息
    public static final String ADMIN_ADD_ONCE_PERMISSION_INFO = BASE_URL + "/api/admin/permission/adminAddOncePermissionInfo";

    // 超管查询权限全部信息
    public static final String ADMIN_SELECT_ALL_PERMISSION_INFO = BASE_URL + "/api/admin/permission/adminSelectAllPermissionInfo";

    // 超管通过权限自增ID，查询拥有该权限全部用户对应的用户角色权限并集信息
    public static final String ADMIN_SELECT_HAVE_PERMISSION_USER_INFO_BY_USERNAME = BASE_URL + "/api/user/adminSelectAllInfoUserAndRoleAndPermissionByPermissionId";

    // 超管通过权限ID和旧权限名更新权限名
    public static final String ADMIN_UPDATE_PERMISSION_NAME_BY_PERMISSION_ID_AND_OLD_PERMISSION_NAME = BASE_URL + "/api/admin/permission/adminUpdatePermissionNameByPermissionIdAndOldPermissionName";

    // 超管通过权限ID和旧权限描述更新权限描述
    public static final String ADMIN_UPDATE_PERMISSION_DESCRIPTION_BY_PERMISSION_ID_AND_OLD_PERMISSION_DESCRIPTION = BASE_URL + "/api/admin/permission/adminUpdatePermissionDescriptionByPermissionIdAndOldPermissionDescription";

    // 超管通过权限实体删除一条对应权限信息
    public static final String ADMIN_DELETE_ONCE_PERMISSION_INFO_BY_PERMISSION_ENTITY = BASE_URL + "/api/admin/permission/adminDeleteOncePermissionInfoByPermissionEntity";

    // 超管通过权限ID查询权限信息
    public static final String ADMIN_SELECT_PERMISSION_INFO_BY_PERMISSION_ID = BASE_URL + "/api/admin/permission/adminSelectPermissionInfoByPermissionId";

    // 超管通过权限名称查询权限信息
    public static final String ADMIN_SELECT_PERMISSION_INFO_BY_PERMISSION_NAME = BASE_URL + "/api/admin/permission/adminSelectPermissionInfoByPermissionName";

    /* ************************ 5.用户角色相关API接口 *************************/

    // 超管通过用户ID和角色ID添加一条用户角色信息
    public static final String ADMIN_ADD_ONCE_USER_ROLE_INFO_BY_USERID_AND_ROLE_ID = BASE_URL + "/api/admin/user/role/adminAddOnceUserRoleInfoByUserIdAndRoleId";

    // 超管通过用户角色ID+旧用户ID+旧角色ID更新用户角色信息
    public static final String ADMIN_UPDATE_USER_ROLE_INFO_BY_OLD_USER_ID_AND_OLD_ROLE_ID = BASE_URL + "/api/admin/user/role/adminUpdateURInfoByURIdAndOldUserIdAndOldRoleId";

    // 超管通过用户ID查询该用户对应的角色集合
    public static final String ADMIN_SELECT_USER_ROLE_INFO_BY_USERID = BASE_URL + "/api/admin/user/role/adminSelectOnlyUserToRoleListInfoByUserId";

    // 超管通过用户ID查询该用户拥有全部对应的用户角色并集信息
    public static final String ADMIN_SELECT_USER_ROLE_INFO_BY_USER_ID = BASE_URL + "/api/admin/user/role/adminSelectUserAndRoleInfoByUserId";

    // 超管通过用户名查询该用户拥有全部对应的用户角色并集信息
    public static final String ADMIN_SELECT_USER_ROLE_INFO_BY_USERNAME = BASE_URL + "/api/admin/user/role/adminSelectOnlyUserToRoleListInfoByUserName";

    // 超管通过角色ID查询拥有该角色的全部用户角色并集信息
    public static final String ADMIN_SELECT_HAVE_ROLE_USER_INFO_BY_ROLE_ID = BASE_URL + "/api/admin/user/role/adminSelectAllInfoUserAndRoleByRoleId";

    // 超管通过角色名查询拥有该角色的全部用户角色并集信息
    public static final String ADMIN_SELECT_HAVE_ROLE_USER_INFO_BY_ROLE_NAME = BASE_URL + "/api/admin/user/role/adminSelectAllInfoUserAndRoleByRoleName";
    /* ************************ 6.角色权限相关API接口 *************************/

    // 超管新增一条角色权限信息
    public static final String ADMIN_ADD_ONCE_ROLE_PERMISSION_INFO_BY_ROLE_ID_AND_PERMISSION_ID = BASE_URL + "/api/admin/role/permission/adminAddOnceRolePermissionInfoByRoleIdAndPermissionId";

    // 超管通过角色权限ID+旧角色ID+旧权限ID更新角色权限信息
    public static final String ADMIN_UPDATE_ROLE_PERMISSION_INFO_BY_OLD_ROLE_ID_AND_OLD_PERMISSION_ID = BASE_URL + "/api/admin/role/permission/adminUpdateRPInfoByRPIdAndOldRoleIdAndOldPermissionId";

    // 超管通过权限ID查询拥有该权限全部的角色信息
    public static final String ADMIN_SELECT_ROLE_INFO_BY_PERMISSION_ID = BASE_URL + "/api/admin/role/permission/adminSelectRoleInfoByPermissionId";

    // 超管通过权限名查询拥有该权限全部的角色信息
    public static final String ADMIN_SELECT_ROLE_INFO_BY_PERMISSION_NAME = BASE_URL + "/api/admin/role/permission/adminSelectRoleInfoByPermissionName";

    // 超管通过角色ID查询拥有该角色全部的权限信息
    public static final String ADMIN_SELECT_PERMISSION_INFO_BY_ROLE_ID = BASE_URL + "/api/admin/role/permission/adminSelectPermissionInfoByRoleId";

    // 超管通过角色名查询拥有该角色全部的权限信息
    public static final String ADMIN_SELECT_PERMISSION_INFO_BY_ROLE_NAME = BASE_URL + "/api/admin/role/permission/adminSelectPermissionInfoByRoleName";

    /* ************************ 7.用户角色权限相关API接口 *************************/

    // 超管通过用户名查询该用户拥有的权限集合
    public static final String ADMIN_SELECT_USER_THE_PERMISSION_INFO_BY_USERNAME = BASE_URL + "/api/admin/user/role/permission/adminSelectUserThePermissionInfoByUserName";

    // 超管查询全部用户角色权限对应的并集信息
    public static final String ADMIN_SELECT_ALL_INFO_USER_AND_ROLE_AND_PERMISSION = BASE_URL + "/api/admin/user/role/permission/adminSelectAllInfoUserAndRoleAndPermission";

    // 超管通过用户ID查询拥有该权限全部用户对应的用户角色权限并集信息
    public static final String ADMIN_SELECT_ALL_INFO_USER_AND_ROLE_PERMISSION_BY_USER_ID = BASE_URL + "/api/admin/user/role/permission/adminSelectAllInfoUserAndRoleAndPermissionByUserId";

    // 超管通过用户名查询拥有该权限全部用户对应的用户角色权限并集信息
    public static final String ADMIN_SELECT_ALL_INFO_USER_AND_ROLE_PERMISSION_BY_USER_NAME = BASE_URL + "/api/admin/user/role/permission/adminSelectAllInfoUserAndRoleAndPermissionByUserName";

    // 超管通过用户真实姓名查询拥有该权限全部用户对应的用户角色权限并集信息
    public static final String ADMIN_SELECT_ALL_INFO_USER_AND_ROLE_PERMISSION_BY_REAL_NAME = BASE_URL + "/api/admin/user/role/permission/adminSelectAllInfoUserAndRoleAndPermissionByRealName";

    // 超管通过权限ID查询拥有该权限全部用户对应的用户角色权限并集信息
    public static final String ADMIN_SELECT_ALL_INFO_USER_AND_ROLE_PERMISSION_BY_PERMISSION_ID = BASE_URL + "/api/admin/user/role/permission/adminSelectAllInfoUserAndRoleAndPermissionByPermissionId";

    // 超管通过权限名查询拥有该权限全部用户对应的用户角色权限并集信息
    public static final String ADMIN_SELECT_ALL_INFO_USER_AND_ROLE_PERMISSION_BY_PERMISSION_NAME = BASE_URL + "/api/admin/user/role/permission/adminSelectAllInfoUserAndRoleAndPermissionByPermissionName";

    /* ************************ 8.用户实名认证相关API接口 *************************/

    // 超管通过用户ID查询对应用户+实名并集信息
    public static final String ADMIN_SELECT_CER_ALL_INFO_BY_USERID = BASE_URL + "/api/user/certification/adminSelectUserCerAllInfoByUserId";

    // 超管通过真实姓名查询对应用户+实名并集信息
    public static final String ADMIN_SELECT_CER_ALL_INFO_BY_USER_REAL_NAME = BASE_URL + "/api/user/certification/adminSelectUserCerAllInfoByUserRealName";

    // 超管通过身份证号查询对应用户+实名并集信息
    public static final String ADMIN_SELECT_CER_ALL_INFO_BY_USER_ID_CARD = BASE_URL + "/api/user/certification/adminSelectUserCerAllInfoByByUserIdCard";

    // 超管查询已实名用户全部信息
    public static final String ADMIN_SELECT_ALL_USER_CER_INFO = BASE_URL + "/api/user/certification/adminSelectAllUserCerInfo";
}

