/**
 * Created by star on 5/10/14.
 */
var kyjhModel;
var model;
$(function(){
    // 绑定按钮事件
    kyjhModel = new KYJHModel();
    model = new AuditActions();

    bindActions();

    kyjhModel.loadBureau();
    ko.applyBindings(kyjhModel);

});

function AuditActions() {
    var self = this;
    
    var _default = {
        autoOpen: false,
        height: $(window).height()/2,
        width: 500,
        title: "",
        position: [($(window).width() - 500), 0],
        maxWidth: 870,
        maxHeight: $(window).height()

    };

    //客运计划时刻表框
    self.kyjh_time = null;
    //客运计划经由框
    self.kyjh_routing = null;
    //运行线时刻表框
    self.yxx_time = null;
    //运行线经由框
    self.yxx_routing = null;
    //图形对比框
    self.compare = null;

    self._getDialog = function(page, options) {
        self._default = {
            autoOpen: options.autoOpen || _default.autoOpen,
            height: options.height || _default.height,
            width: options.width || _default.width,
            title: options.title || _default.title,
            position: options.position || _default.position,
            maxWidth: _default.maxWidth,
            maxHeight: _default.maxHeight,
            closeText: "关闭",
            close: options.close
        };
        var $dialog = $('<div class="dialog" style="overflow: hidden"></div>')
            .html('<iframe src="' + page + '" width="100%" height="100%" marginWidth=0 frameSpacing=0 marginHeight=0 scrolling=auto frameborder="0px"></iframe>')
            .dialog(self._default);
        return $dialog;
    };

    //客运计划时刻表
    self.open_kyjh_time = function(params, options) {
        if(self.kyjh_time_opened()) {
            self.update_kyjh_time(params)
        } else {
            options.close = self.close_kyjh_time;
            var url = "audit/plan/timetable/" + kyjhModel.bureau().code + "/" + params;
            self.kyjh_time = self._getDialog(url, options);
            self.kyjh_time.dialog("open");
        }
    }

    self.close_kyjh_time = function() {
        self.kyjh_time.dialog("close");
        self.kyjh_time.remove();
        self.kyjh_time = null;
        $("#kyjh_time").attr('checked', false);
    };

    self.kyjh_time_opened = function() {
        return self.kyjh_time != null;
    }

    self.update_kyjh_time = function(params) {
        $(self.kyjh_time).find("iframe").attr("src", "audit/plan/timetable/" + kyjhModel.bureau().code + "/" + params);
    }

    //客运计划时经由
    self.open_kyjh_routing = function(params, options) {
        if(self.kyjh_routing_opened()) {
            self.update_kyjh_routing(params);
        } else {
            options.close = self.close_kyjh_routing;
            self.kyjh_routing = self._getDialog("audit/plan/routing", options);
            self.kyjh_routing.dialog("open");
        }
    }

    self.close_kyjh_routing = function() {
        self.kyjh_routing.dialog("close");
        self.kyjh_routing = null;
        $("#kyjh_routing").attr('checked', false);
    }

    self.update_kyjh_routing = function(params) {

    }

    self.kyjh_routing_opened = function() {
        return self.kyjh_routing != null;
    }
    //运行线时刻表
    self.open_yxx_time = function(params, options) {
        if(self.yxx_time_opened()) {
            self.update_yxx_time(params);
        } else {
            options.close = self.close_yxx_time;
            var url = "audit/line/timetable/" + kyjhModel.bureau().name + "/" + params;
            self.yxx_time = self._getDialog(url, options);
            self.yxx_time.dialog("open");
        }

    }

    self.close_yxx_time = function() {
        self.yxx_time.dialog("close");
        self.yxx_time = null;
        $("#yxx_time").attr('checked', false);
    }

    self.update_yxx_time = function(params) {
        $(self.yxx_time).find("iframe").attr("src", "audit/line/timetable/" + kyjhModel.bureau().name + "/" + params);
    }

    self.yxx_time_opened = function() {
        return self.yxx_time != null;
    }
    //运行线经由
    self.open_yxx_routing = function(params, options) {
        if(self.yxx_routing_opened()) {
            self.update_yxx_routing(params);
        } else {
            options.close = self.close_yxx_routing;
            self.yxx_routing = self._getDialog("audit/plan/routing", options);
            self.yxx_routing.dialog("open");
        }
    }

    self.close_yxx_routing = function() {
        self.yxx_routing.dialog("close");
        self.yxx_routing = null;
        $("#yxx_routing").attr('checked', false);
    }

    self.update_yxx_routing = function(params) {

    }

    self.yxx_routing_opened = function() {
        return self.yxx_routing != null;
    }
    //图形对比框
    self.open_compare = function(params, options) {
        if(self.compare_opened()) {
            self.update_compare(params);
        } else {
            options.close = self.close_compare;
            var queryStr = "date=" + $("#date_selector").val();
            queryStr += "&bureau=" + kyjhModel.bureau().code;
            queryStr += "&" + params;
            self.compare = self._getDialog("audit/planline?" + queryStr, options);
            self.compare.dialog("open");
        }
    }

    self.close_compare = function() {
        self.compare.dialog("close");
        self.compare = null;
    }

    self.update_compare = function(params) {
        var queryStr = "date=" + $("#date_selector").val();
        queryStr += "&bureau=" + kyjhModel.bureau().code;
        queryStr += "&" + params;
        $(self.kyjh_time).find("iframe").attr("src", "audit/planline?" + queryStr);
    }

    self.compare_opened = function() {
        return self.compare != null;
    }

    self.select_all = function() {
        $("#left_table").find("thead input[type=checkbox]").click(function() {
            if($(this).is(':checked')){
                $("#left_table").find("tbody input[type=checkbox]").each(function (index, ele) {
                    $(ele).prop('checked', true);
                })
            } else {
                $("#left_table").find("tbody input[type=checkbox]").each(function (index, ele) {
                    $(ele).prop('checked', false);
                })
            }
        })
    }
}


function bindActions() {

    $("#date_selector").datepicker({format: "yyyy-mm-dd"}).on('changeDate', function (ev) {
        kyjhModel.loadKYJH(moment(ev.date).format("YYYYMMDD"));
        kyjhModel.loadYXX(moment(ev.date).format("YYYYMMDD"));
    });;
    var date = $.url().param("date");
    if (date) {
        $("#date_selector").val(date);
    } else {
        $("#date_selector").datepicker('setValue', new Date());
    }

    model.select_all();

    //bind actions
    $("#kyjh_time").click(function() {
        if($(this).is(':checked')){
            var params = {};
//            params.train_id = "7292688";
            model.open_kyjh_time("", {title: "客运开行计划时刻表"});
        } else {
            model.close_kyjh_time();
        }
    });
    $("#kyjh_routing").click(function() {
        if($(this).is(':checked')){
            model.open_kyjh_routing("", {title: "客运开行计划经由"});
        } else {
            model.close_kyjh_routing();
        }
    });
    $("#yxx_time").click(function() {
        if($(this).is(':checked')){
            model.open_yxx_time("", {title: "运行线时刻表"})
        } else {
            model.close_yxx_time();
        }
    });
    $("#yxx_routing").click(function() {
        if($(this).is(':checked')){
            model.open_yxx_routing("", {title: "运行线经由"});
        } else {
            model.close_yxx_routing();
        }
    });
    $("#compare").click(function() {
        if(($("input[name='plan']:checked").size() != 1) || ($("input[name='line']:checked").size() > 1)){
            return false;
        }
        var plans = "";
        var lines = "";
        /*
        $("input[name='plan']:checked").each(function(index, ele) {
            plans += $(ele).val() + ",";
        });
        $($("input[name='line']:checked")).each(function(index, ele) {
            lines += $(ele).val() + ",";
        })
        */
        if($("input[name='plan']:checked").size() == 1) {
            plans = $("input[name='plan']:checked").val();
        }
        if($($("input[name='line']:checked")).size() == 1) {
            lines = $($("input[name='line']:checked")).val();
        }
        if(plans == "" && lines == "") {

        } else {
            model.open_compare("plans=" + plans + "&lines=" + lines, {title: "图形对比", height: $(window).height(), width: 700});
        }
    })
}

function KYJHModel() {
    var self = this;

    self.kyjhTable = ko.observableArray();

    self.yxxTable = ko.observableArray();

    self.bureauList = ko.observableArray();

    self.bureau = ko.observable();

    self.loadBureau = function() {
        $.ajax({
            url: "base/bureau/all",
            method: "GET",
            contentType: "application/json; charset=UTF-8"
        }).done(function(list) {
            self.bureauList.removeAll();
            for( var i = 0; i < list.length; i++) {
                self.bureauList.push(list[i]);
            }
        }).fail(function() {

        }).always(function() {
            kyjhModel.loadKYJH(moment($("#date_selector").val()).format("YYYYMMDD"));
            kyjhModel.loadYXX(moment($("#date_selector").val()).format("YYYYMMDD"));
        })
    }

    self.currentDate = null;

    self.paramDate = null;

    self.loadKYJH = function(date) {
        self.paramDate = date;
        $.ajax({
            url: "audit/plan/runplan/" + date + "/" + self.bureau().code + "/1",
            method: "GET",
            contentType: "application/json; charset=UTF-8"
        }).done(function(list) {
            self.kyjhTable.removeAll();
            for( var i = 0; i < list.length; i++) {
                self.kyjhTable.push(list[i]);
            }
            // 表头固定
            $("#left_table").freezeHeader();
        }).fail(function() {

        }).always(function() {

        })
    }

    self.loadYXX = function(date) {
        $.ajax({
            url: "audit/plan/runline/" + date + "/" + self.bureau().name + "/1",
            method: "GET",
            contentType: "application/json; charset=UTF-8"
        }).done(function(list) {
            self.yxxTable.removeAll();
            for( var i = 0; i < list.length; i++) {
                self.yxxTable.push(list[i]);
            }
            // 表头固定
            $("#right_table").freezeHeader();
        }).fail(function() {

        }).always(function() {

        })
    }

    self.update_kyjh_panel = function(ev) {
        model.open_kyjh_time(ev.id, {title: "客运开行计划时刻表"});
        model.open_kyjh_routing(ev.id, {title: "客运开行计划经由"});
    }

    self.update_yxx_panel = function(ev) {
        model.open_yxx_time(ev.id, {title: "运行线时刻表"});
        model.open_yxx_routing(ev.id, {title: "运行线经由"});
    }
}