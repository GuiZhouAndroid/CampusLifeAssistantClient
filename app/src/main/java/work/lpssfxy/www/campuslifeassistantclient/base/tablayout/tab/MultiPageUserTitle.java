package work.lpssfxy.www.campuslifeassistantclient.base.tablayout.tab;

/**
 * created by on 2021/11/26
 * 描述：开发者后台中心，角色管理TabLayout标题枚举类
 *
 * @author ZSAndroid
 * @create 2021-11-26-14:51
 */
public enum MultiPageUserTitle {

    全部详情(0);

    private final int position;

    MultiPageUserTitle(int pos) {
        position = pos;
    }

    public static MultiPageUserTitle getPage(int position) {
        return MultiPageUserTitle.values()[position];
    }

    public static int size() {
        return MultiPageUserTitle.values().length;
    }

    public static String[] getPageNames() {
        MultiPageUserTitle[] pages = MultiPageUserTitle.values();
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

