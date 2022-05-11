package work.lpssfxy.www.campuslifeassistantclient.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by on 2022/5/11
 * 描述：新浪疫情实体类
 *
 * @author ZSAndroid
 * @create 2022-05-11-10:20
 */
@NoArgsConstructor
@Data
public class EpidemicBean {
    private String data_title;
    private Data data;

    @NoArgsConstructor
    @lombok.Data
    public static class Data {
        private String times;//统计数据截至日期
        private String mtime;//统计数据截至日期
        private String cachetime;//	缓存时间
        private String gntotal;//累计确诊
        private String deathtotal;//累计死亡
        private String sustotal;//有病例城市个数
        private String curetotal;//累计治愈
        private String econNum;//现存确诊
        private String heconNum;//现存重症确诊
        private String asymptomNum;
        private String jwsrNum;//境外累计输入病例
        private AddDaily add_daily;//全国疫情数据
        private java.util.List<JwsrTop> jwsrTop;//境外输入城市及确诊个数
        private java.util.List<List> list;//城市病例详情
        private Othertotal othertotal;
        private java.util.List<Otherlist> otherlist;
        private java.util.List<Otherhistorylist> otherhistorylist;
        private java.util.List<Historylist> historylist;
        private java.util.List<Worldlist> worldlist;
        private CaseClearCityInfo caseClearCityInfo;

        @NoArgsConstructor
        @lombok.Data
        public static class AddDaily {
            private int addcon;
            private int addsus;
            private int adddeath;
            private int addcure;
            private String wjw_addsus;
            private String addcon_new;
            private String adddeath_new;
            private String addcure_new;
            private String wjw_addsus_new;
            private String addecon_new;
            private String addhecon_new;
            private String addjwsr;
            private String addasymptom;
        }

        @NoArgsConstructor
        @lombok.Data
        public static class Othertotal {
            private String certain;
            private String uncertain;
            private String die;
            private String recure;
            private String certain_inc;
            private String uncertain_inc;
            private String die_inc;
            private String recure_inc;
            private String ecertain;
            private String ecertain_inc;
        }

        @NoArgsConstructor
        @lombok.Data
        public static class CaseClearCityInfo {
            private int ljClearCityNum;
            private int ljNoClearCityNum;
            private int ljCityNum;
        }

        @NoArgsConstructor
        @lombok.Data
        public static class JwsrTop {
            private String jwsrNum;
            private String name;
            private String ename;
        }

        @NoArgsConstructor
        @lombok.Data
        public static class List {
            private String name;
            private String ename;
            private String value;
            private String conadd;
            private String hejian;
            private String econNum;
            private String susNum;
            private String deathNum;
            private String asymptomNum;
            private String cureNum;
            private String zerodays;
            private String jwsr;
            private String jwsrNum;
            private String showCurData;
            private Adddaily adddaily;
            private java.util.List<City> city;

            @NoArgsConstructor
            @lombok.Data
            public static class Adddaily {
                private String conadd;
                private String deathadd;
                private String cureadd;
                private String econadd;
                private String conadd_n;
                private String deathadd_n;
                private String cureadd_n;
            }

            @NoArgsConstructor
            @lombok.Data
            public static class City {
                private String name;
                private String conNum;
                private String conadd;
                private String conadd_str;
                private String hejian;
                private String susNum;
                private String cureNum;
                private String deathNum;
                private String mapName;
                private String citycode;
                private String econNum;
                private String zerodays;
                private String jwsr;
                private String asymptomNum;
            }
        }

        @NoArgsConstructor
        @lombok.Data
        public static class Otherlist {
            private String conNum;
            private String susNum;
            private String cureNum;
            private String deathNum;
            private String conadd;
            private String susadd;
            private String cureadd;
            private String deathadd;
            private String econNum;
            private String name;
            private String citycode;
            private String value;
            private int is_show_entrance;
            private int is_show_map;
        }

        @NoArgsConstructor
        @lombok.Data
        public static class Otherhistorylist {
            private String certain;
            private String uncertain;
            private String die;
            private String recure;
            private String certain_inc;
            private String uncertain_inc;
            private String die_inc;
            private String recure_inc;
            private String date;
        }

        @NoArgsConstructor
        @lombok.Data
        public static class Historylist {
            private String date;
            private String cn_conNum;
            private String cn_deathNum;
            private String cn_cureNum;
            private String cn_susNum;
            private String cn_econNum;
            private String cn_heconNum;
            private String cn_deathRate;
            private String cn_cureRate;
            private String is_show;
            private String wjw_susNum;
            private String wuhan_conNum;
            private String wuhan_deathNum;
            private String wuhan_cureNum;
            private Object wuhan_susNum;
            private String cn_conadd;
            private String cn_jwsrNum;
            private String cn_addjwsrNum;
            private String cn_asymptomNum;
        }

        @NoArgsConstructor
        @lombok.Data
        public static class Worldlist {
            private String name;
            private String value;
            private String susNum;
            private String deathNum;
            private String cureNum;
            private String econNum;
            private String conNum;
            private String conadd;
            private String susadd;
            private String cureadd;
            private String deathadd;
            private String citycode;
            private int is_show_entrance;
            private int is_show_map;
        }
    }
}
