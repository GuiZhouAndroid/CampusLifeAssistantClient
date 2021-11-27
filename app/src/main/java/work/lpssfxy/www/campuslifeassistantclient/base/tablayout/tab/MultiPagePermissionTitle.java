package work.lpssfxy.www.campuslifeassistantclient.base.tablayout.tab;

/**
 * created by on 2021/11/26
 * 描述：开发者后台中心，权限管理TabLayout标题枚举类
 *
 * @author ZSAndroid
 * @create 2021-11-26-14:51
 */
public enum MultiPagePermissionTitle {

    列表概况(0),
    专属权限(1);
//    专属用户(2);

    private final int position;

    MultiPagePermissionTitle(int pos) {
        position = pos;
    }

    public static MultiPagePermissionTitle getPage(int position) {
        return MultiPagePermissionTitle.values()[position];
    }

    public static int size() {
        return MultiPagePermissionTitle.values().length;
    }

    public static String[] getPageNames() {
        MultiPagePermissionTitle[] pages = MultiPagePermissionTitle.values();
        String[] pageNames = new String[pages.length];
        for (int i = 0; i < pages.length; i++) {
            pageNames[i] = pages[i].name();
        }
        return pageNames;
    }

    public int getPosition() {
        return position;
    }
}

