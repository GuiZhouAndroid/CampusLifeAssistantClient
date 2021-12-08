package work.lpssfxy.www.campuslifeassistantclient.base.tablayout.tab;

/**
 * created by on 2021/11/27
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-11-27-22:49
 */
public enum MultiPageUserPermissionTitle {

    全部详情(0),
    专属权限(1),
    专属用户(2);

    private final int position;

    MultiPageUserPermissionTitle(int pos) {
        position = pos;
    }

    public static MultiPageUserPermissionTitle getPage(int position) {
        return MultiPageUserPermissionTitle.values()[position];
    }

    public static int size() {
        return MultiPageUserPermissionTitle.values().length;
    }

    public static String[] getPageNames() {
        MultiPageUserPermissionTitle[] pages = MultiPageUserPermissionTitle.values();
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


