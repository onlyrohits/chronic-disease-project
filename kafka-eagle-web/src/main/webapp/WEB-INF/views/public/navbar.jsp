<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Navigation -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse"
                data-target=".navbar-ex1-collapse">
            <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
            <span class="icon-bar"></span> <span class="icon-bar"></span>
        </button>
        <p class="navbar-brand">慢性病大数据监控平台</p>
        <div class="modal fade" aria-labelledby="mfModalLabel"
             aria-hidden="true" id="ke_account_reset_dialog" tabindex="-1"
             role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button class="close" type="button" data-dismiss="modal">×</button>
                        <h4 class="modal-title" id="keModalLabel">Reset password</h4>
                    </div>
                    <!-- /.row -->
                    <form role="form" action="/ke/account/reset/" method="post"
                          onsubmit="return contextFormValid();return false;">
                        <div class="modal-body">
                            <fieldset class="form-horizontal">
                                <div class="form-group">
                                    <label for="path" class="col-sm-2 control-label">New</label>
                                    <div class="col-sm-10">
                                        <input id="mf_new_password_name" name="ke_new_password_name"
                                               type="password" class="form-control" maxlength="16"
                                               placeholder="New Password">
                                    </div>
                                </div>
                                <div id="alert_mssage" style="display: none"
                                     class="alert alert-danger">
                                    <label> Passwords can only be data and letters or
                                        special symbols .</label>
                                </div>
                            </fieldset>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default"
                                    data-dismiss="modal">Cancle
                            </button>
                            <button type="submit" class="btn btn-primary" id="create-btn">Submit
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <!-- Top Menu Items -->
    <ul class="nav navbar-right top-nav">
        <li class="dropdown"><a href="#" class="dropdown-toggle"
                                data-toggle="dropdown"><i class="fa fa-sitemap"></i>
            ${clusterAlias} </a></li>
        <li class="dropdown"><a href="#" class="dropdown-toggle"
                                data-toggle="dropdown"><i class="fa fa-bookmark"></i> V1.1.9 </a></li>
        <li class="dropdown"><a href="#" class="dropdown-toggle"
                                data-toggle="dropdown" aria-expanded="false"><i
                class="fa fa-user"></i> ${LOGIN_USER_SESSION.realname} <b
                class="caret"></b></a>
            <ul class="dropdown-menu">
                <li><a name="ke_account_reset" href="#"><i
                        class="fa fa-fw fa-gear"></i> 修改信息</a></li>
                <li><a href="/ke/account/signout"><i
                        class="fa fa-fw fa-power-off"></i> 退出 </a></li>
            </ul>
        </li>
    </ul>

    <!-- Sidebar Menu Items - These collapse to the responsive navigation menu on small screens -->
    <div class="collapse navbar-collapse navbar-ex1-collapse">
        <ul class="nav navbar-nav side-nav">
            <li><a href="#" data-toggle="collapse" data-target="#demo2-1">
                <i class="fa fa-fw fa-cloud"></i> 集群资源管理 <i
                    class="fa fa-fw fa-caret-down"></i></a>
                <ul id="demo2-1" class="collapse">

                    <%-- Kafka状况总览 --%>
                    <li id="navbar_dash"><a href="/ke"><i
                            class="fa fa-fw fa-dashboard"></i> Kafka状况总览 </a></li>

                    <%-- 集群管理 --%>
                    <li><a href="#" data-toggle="collapse" data-target="#demo2">
                        <i class="fa fa-fw fa-cloud"></i> Kafka集群管理 <i
                            class="fa fa-fw fa-caret-down"></i></a>
                        <ul id="demo2" class="collapse">
                            <li id="navbar_cli1"><a href="/ke/cluster/info"><i
                                    class="fa fa-sitemap fa-fw"></i> 集群信息 </a></li>
                            <li id="navbar_cli2"><a href="/ke/cluster/multi"><i
                                    class="fa fa-maxcdn fa-fw"></i> 多集群资源 </a></li>
                            <li id="navbar_zk"><a href="/ke/cluster/zkcli"><i
                                    class="fa fa-terminal fa-fw"></i> Zookeeper客户端管理 </a></li>
                        </ul>
                    </li>

                    <%-- Kafka主题管理 --%>
                    <li><a href="#" data-toggle="collapse" data-target="#demo"><i
                            class="fa fa-fw fa-comments-o"></i> Kafka主题 <i
                            class="fa fa-fw fa-caret-down"></i></a>
                        <ul id="demo" class="collapse">
                            <li id="navbar_create"><a href="/ke/topic/create"><i
                                    class="fa fa-edit fa-fw"></i> 创建主题 </a></li>
                            <li id="navbar_list1"><a href="/ke/topic/list"><i
                                    class="fa fa-table fa-fw"></i> 展示主题 </a></li>
                            <li id="navbar_list2"><a href="/ke/topic/message"><i
                                    class="fa fa-file-text fa-fw"></i> 查询消息 </a></li>
                            <li id="navbar_list3"><a href="/ke/topic/mock"><i
                                    class="fa fa-maxcdn fa-fw"></i> 发送消息 </a></li>
                        </ul>
                    </li>


                    <%-- Kafka客户端管理 --%>
                    <li id="navbar_consumers"><a href="/ke/consumers"><i
                            class="fa fa-fw fa-users"></i> Kafka消费者 </a></li>

                    <%-- 监控信息 --%>
                    <li><a href="#" data-toggle="collapse" data-target="#demo4"><i
                            class="fa fa-fw fa-eye"></i> Kafka监控信息 <i
                            class="fa fa-fw fa-caret-down"></i></a>
                        <ul id="demo4" class="collapse">
                            <li id="navbar_cli3"><a href="/ke/metrics/brokers"><i
                                    class="fa fa-sitemap fa-fw"></i> Kafka服务器状态 </a></li>
                            <li id="navbar_cli4"><a href="/ke/metrics/trend"><i
                                    class="fa fa-bar-chart-o fa-fw"></i> 趋势概览 </a></li>
                        </ul>
                    </li>

                    <%-- 状态警报 --%>
                    <li><a href="#" data-toggle="collapse" data-target="#demo1"><i
                            class="fa fa-fw fa-bell"></i> 警报 <i
                            class="fa fa-fw fa-caret-down"></i></a>
                        <ul id="demo1" class="collapse">
                            <li id="navbar_add"><a href="/ke/alarm/add"><i
                                    class="fa fa-info-circle fa-fw"></i> 添加警报 </a></li>
                            <li id="navbar_modify"><a href="/ke/alarm/modify"><i
                                    class="fa fa-edit fa-fw"></i> 修改警报 </a></li>
                        </ul>
                    </li>

                </ul>
            </li>


            <li><a href="#" data-toggle="collapse" data-target="#offlineLearning"><i
                    class="fa fa-fw fa-bar-chart-o"></i> 模式挖掘和匹配 <i
                    class="fa fa-fw fa-caret-down"></i></a>
                <ul id="offlineLearning" class="collapse">
                    <li><a href="/ke/offlineLearning/toAdd?type=0"><i
                            class="fa fa-plus-circle"></i> 新建学习任务</a></li>
                    <li><a href="/ke/offlineLearning/getAllConfigure"><i
                            class="fa fa-fw fa-bar-chart"></i> 管理学习任务</a></li>
                    <li><a href="/ke/onlineLearning/macthingViewPage"><i
                            class="fa fa-fw fa-bar-chart"></i> 在线模式匹配</a></li>
                    <li><a href="/ke/onlineLearning/managePattern"><i
                            class="fa fa-fw fa-bar-chart"></i> 在线模式管理</a></li>
                </ul>
            </li>

            <li><a href="#" data-toggle="collapse" data-target="#demo5"><i
                    class="fa fa-fw fa-user"></i> 用户分析 <i
                    class="fa fa-fw fa-caret-down"></i></a>
                <ul id="demo5" class="collapse">
                    <li id="navbar_realtime"><a href="/ke/patient_analysis/realTime"><i
                            class="fa fa-fw fa-bar-chart"></i> 实时数据</a></li>
                    <li id="navbar_perPatientInfo"><a href="/ke/patient_analysis/perPatientInfo"><i
                            class="fa fa-fw fa-bar-chart"></i> 个人数据分析</a></li>
                    <li id="navbar_wholePatientInfo"><a href="/ke/patient_analysis/wholePatientInfo"><i
                            class="fa fa-fw fa-bar-chart"></i> 综合数据分析</a></li>
                    <li id="navbar_history"><a href="/ke/patient_analysis/history/encounter"><i
                            class="fa fa-fw fa-bar-chart"></i>个人历史数据</a></li>
                    <li id="navbar_history_compre"><a href="/ke/patient_analysis/history/goToMultiHistoryPage"><i
                            class="fa fa-fw fa-bar-chart"></i>多用户历史数据</a></li>
                </ul>
            </li>

            <li><a href="#" data-toggle="collapse" data-target="#demo6"><i
                    class="fa fa-fw fa-eye"></i> 实时定位 <i
                    class="fa fa-fw fa-caret-down"></i></a>
                <ul id="demo6" class="collapse">
                    <li><a href="/ke/monitor/whole-country"><i
                            class="fa fa-map-o fa-fw"></i> 全国疾病定位 </a></li>
                    <li><a href="/ke/monitor/location"><i
                            class="fa fa-map-marker fa-fw"></i> 实时定位 </a></li>
                    <li><a href="/ke/monitor/disease_history_mon"><i
                            class="fa fa-map-marker fa-fw"></i> 历史数据分析 </a></li>
                    <li><a href="/ke/monitor/disease_history_timeline"><i
                            class="fa fa-map-marker fa-fw"></i> 地域数据分析 </a></li>
                </ul>
            </li>
            <li><a href="#" data-toggle="collapse" data-target="#demo7"><i
                    class="fa fa-fw fa-search"></i> 综合分析 <i
                    class="fa fa-fw fa-caret-down"></i></a>
                <ul id="demo7" class="collapse">
                    <li id="navbar_mood"><a href="/ke/comprehensive/mood"><i
                            class="fa  fa-smile-o fa-fw"></i>情绪分析</a></li>
                    <li id="navbar_sport"><a href="/ke/comprehensive/exercise"><i
                            class="fa fa-futbol-o fa-fw"></i> 运动习惯分析</a></li>
                    <li id="navbar_filt"><a href="/ke/comprehensive/filtrate"><i
                            class="fa fa-street-view fa-fw"></i> 筛选分析</a></li>
                </ul>
            </li>

            <c:if test="${WHETHER_SYSTEM_ADMIN==1}">
                <li><a href="#" data-toggle="collapse" data-target="#demo3"><i
                        class="fa fa-fw fa-cog"></i> 系统 <i
                        class="fa fa-fw fa-caret-down"></i></a>
                    <ul id="demo3" class="collapse">
                        <li><a href="/ke/system/user"><i class="fa fa-user fa-fw"></i>
                            用户管理</a></li>
                        <li><a href="/ke/system/role"><i class="fa fa-key fa-fw"></i>
                            角色管理</a></li>
                        <li><a href="/ke/system/resource"><i
                                class="fa fa-folder-open fa-fw"></i> 资源管理 </a></li>
                    </ul>
                </li>
            </c:if>


            <li><a href="/ke/monitor/monitor_maintain"><i
                    class="fa fa-desktop fa-fw"></i> 监视器管理 </a></li>

            <c:if test="${WHETHER_SYSTEM_ADMIN==1}">
                <li id="navbar_visualizer"><a href="/ke/visualizer/visShow"><i
                        class="fa fa-fw fa-users"></i> 综合资源管理</a></li>
            </c:if>
        </ul>
    </div>
    <!-- /.navbar-collapse -->
</nav>
<script type="text/javascript">
    function contextFormValid() {
        var ke_new_password_name = $("#ke_new_password_name").val();
        var reg = /^[u4E00-u9FA5]+$/;
        if (ke_new_password_name.length == 0 || !reg.test(ke_new_password_name)) {
            $("#alert_mssage").show();
            setTimeout(function () {
                $("#alert_mssage").hide()
            }, 3000);
            return false;
        }

        return true;
    }

    //菜单
    function menu() {
        alert("menu");
        var diag = new Dialog();
        diag.Drag = true;
        diag.Title = "菜单编辑";
        diag.URL = '/ke/menu';
        diag.Width = 720;
        diag.Height = 390;
        diag.CancelEvent = function () { //关闭事件
            diag.close();
        };
        diag.show();

    }
</script>