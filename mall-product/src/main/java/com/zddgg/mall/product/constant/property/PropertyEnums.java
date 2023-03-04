package com.zddgg.mall.product.constant.property;

public class PropertyEnums {

    public enum FormShowType {
        Input("0", "输入框"),
        InputNumber("1", "数字输入框"),
        Switch("2", "互斥选择"),
        Select("3", "单选"),
        MultipleSelect("4", "多选"),
        DatePicker("5", "日期选择器"),
        RangeDatePicker("6", "日期范围选择器"),
        RangeDateTimePicker("7", "日期时间范围选择器"),
        TimePicker("8", "时间选择器"),
        RangeTimePicker("9", "时间范围选择器"),
        ;

        public final String code;

        public final String msg;

        FormShowType(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }
}
