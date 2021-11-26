package work.lpssfxy.www.campuslifeassistantclient.base.tablayout.tab;

/**
 * created by on 2021/11/26
 * 描述：开发者后台中心，角色管理TabLayout标题枚举类
 *
 * @author ZSAndroid
 * @create 2021-11-26-14:51
 */
public enum MultiPageRoleTitle {

    全部角色(0),
    添加角色(1),
    角色持有(2),
    角色用户(3);

    private final int position;

    MultiPageRoleTitle(int pos) {
        position = pos;
    }

    public static MultiPageRoleTitle getPage(int position) {
        return MultiPageRoleTitle.values()[position];
    }

    public static int size() {
        return MultiPageRoleTitle.values().length;
    }

    public static String[] getPageNames() {
        MultiPageRoleTitle[] pages = MultiPageRoleTitle.values();
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

