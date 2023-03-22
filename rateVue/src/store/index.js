import Vue from 'vue'
import Vuex from 'vuex'
import {
    Message,
    Notification
} from 'element-ui';
import {
    getRequest
} from "@/utils/api";
import '../utils/stomp'
import '../utils/sockjs'

Vue.use(Vuex)

const now = new Date();

const store = new Vuex.Store({
    state: { //全局对象，用于保存所有组件的公共数据
        routes: [],
        routesAllSameForm:[],
        sessions: {},
        hrs: [],
        currentSession: null,
        // currentHr: JSON.parse(window.sessionStorage.getItem("user")),
        currentHr:{},
        filterKey: '',
        stomp: null,
        isDot: {},
        peract: [],
        score: [],
        changeList: false,
    },
    mutations: { //唯一可以修改state值的方法（同步执行）
        INIT_CURRENTHR(state, hr) {
            state.currentHr = hr;
        },
        initRoutes(state, data) {
            state.routes = data;
        },
        initRoutesAllSameForm(state, data) {
            state.routesAllSameForm = data;
        },
        changeCurrentSession(state, currentSession) {
            Vue.set(state.isDot, state.currentHr.username + '#' + currentSession.username, false);
            state.currentSession = currentSession;
        },
        addMessage(state, msg) {
            let mss = state.sessions[state.currentHr.username + '#' + msg.to];
            if (!mss) {
                // state.sessions[state.currentHr.username+'#'+msg.to] = [];
                Vue.set(state.sessions, state.currentHr.username + '#' + msg.to, []);
            }
            state.sessions[state.currentHr.username + '#' + msg.to].push({
                content: msg.content,
                date: new Date(),
                self: !msg.notSelf
            })
        },
        INIT_DATA(state) {
            //浏览器本地的历史聊天记录可以在这里完成
            let data = localStorage.getItem('vue-chat-session');
            if (data) {
                state.sessions = JSON.parse(data);
            }
        },
        INIT_HR(state, data) {
            state.hrs = data;
        },
        INIT_PERACT(state, data) {
            sessionStorage.setItem("peract", JSON.stringify(data));
            state.peract = data
        },
        INIT_SCORE(state, data) {
            state.score = data
            sessionStorage.setItem("score", JSON.stringify(data));
        },
        INIT_ScoreParticipatesList(state, data) {
            state.score = data
            sessionStorage.setItem("score", JSON.stringify(state.score));
        },
        INIT_initchangeList(state) {
            state.changeList = false
        }
    },
    actions: { //异步执行mutations方法
        connect(context) {
            context.state.stomp = Stomp.over(new SockJS('/ws/ep'));
            context.state.stomp.connect({}, success => {
                context.state.stomp.subscribe('/user/queue/chat', msg => {
                    let receiveMsg = JSON.parse(msg.body);
                    if (!context.state.currentSession || receiveMsg.from != context.state.currentSession.username) {
                        Notification.info({
                            title: '【' + receiveMsg.fromNickname + '】发来一条消息',
                            message: receiveMsg.content.length > 10 ? receiveMsg.content.substr(0, 10) : receiveMsg.content,
                            position: 'bottom-right'
                        })
                        Vue.set(context.state.isDot, context.state.currentHr.username + '#' + receiveMsg.from, true);
                    }
                    receiveMsg.notSelf = true;
                    receiveMsg.to = receiveMsg.from;
                    context.commit('addMessage', receiveMsg);
                })
            }, error => {

            })
        },
        initData(context) {
            context.commit('INIT_DATA')
            getRequest("/chat/hrs").then(resp => {
                if (resp) {
                    context.commit('INIT_HR', resp);
                }
            })
        },
        initsize(context, data) {
            var promise = new Promise((resolve, reject) => {
                getRequest("/system/Experts/activities?expertID=" + data).then(resp => {
                    if (resp) {
                        resp = resp.extend;
                        // if (sessionStorage.getItem('peract')) {
                        // } else {
                        context.commit('INIT_PERACT', resp)
                        // }
                        resolve(resp)
                    }
                })
            })
            return promise
        },
        initAct(context, AI) {
            var Aid = AI.Aid
            var promise = new Promise((resolve, reject) => {
                getRequest("/system/Experts/score?activitiesID=" + Aid + '&expertID=' + AI.Auserid + '&groupId=' + AI.AgroupId).then(value => {
                if (value) {
                    value = value.extend
                    let n = value.participatesList.length;
                    let m = value.scoreitems.length;
                    let p = value.scoresListNoExpert.length;
                    let q = value.infoItems.length;
                    let w = value.infosList.length;
                    for (var i = 0; i < n; i++) {
                        for (var j = 0; j < m; j++) {
                            for (var k = 0; k < p; k++) {
                                if ((value.scoreitems[j]["id"] == value.scoresListNoExpert[k]["scoreItemID"]) &
                                    (value.participatesList[i]["id"] == value.scoresListNoExpert[k]["participantID"]) &
                                    (value.scoreitems[j]["byexpert"] == false)
                                ) {
                                    value.participatesList[i]["score" + j] = value.scoresListNoExpert[k]["score"];
                                }
                            }
                        }

                        for (var s = 0; s < w; s++) {
                            if (
                                value.participatesList[i]["id"] ==
                                value.infosList[s]["participantID"]
                            ) {
                                for (var d = 0; d < q; d++) {
                                    if (
                                        value.infosList[s]["infoItemID"] ==
                                        value.infoItems[d]["id"]
                                    ) {
                                        var name = value.infoItems[d]["name"];
                                        if (value.infoItems[d]["display"] == true) {

                                            value.participatesList[i]["info" + name] = value.infosList[s]["content"]
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 计算一共又多少个不可修改项
                    let count = 0; //
                    for (let temp = 0; temp < value.scoreitems.length; temp++) {
                        if (value.scoreitems[temp].byexpert == false) {
                            count++
                        }
                    }
                    // 如果页面存在sessionStorage
                    if (sessionStorage.getItem("score")) {
                        let JSONscore = JSON.parse(sessionStorage.getItem("score"))
                        let length = value.participatesList.length


                        //如果新老人数不等，即表示有人员变动
                        if (JSONscore.participatesList.length !== value.participatesList.length) {
                            Message.warning('人员顺序发生变化，请注意！')
                            this.state.changeList = true
                        } else {
                            //通过现有的JSONscore.participatesList和新获取的value.participatesList判断对象顺序是否发生改变(通过学生id比较)
                            for (let stuid = 0; stuid < length; stuid++) {
                                //如果对应位置的studentID不相等，表明后台数据库发生变化
                                if (JSONscore.participatesList[stuid].displaySequence !== value.participatesList[stuid].displaySequence ||
                                    JSONscore.participatesList[stuid].studentID !== value.participatesList[stuid].studentID) {
                                    Message.warning('参加人员或人员顺序发生变化，请注意！')
                                    //发生变化changeList修改为true，进入修改操作
                                    this.state.changeList = true
                                    //只要遍历到有变化的地方，就可以停止遍历直接进行修改
                                    break;
                                }
                                //如果对应顺序相等，那么比较里面的详细信息 
                                else if (JSONscore.participatesList[stuid].studentID === value.participatesList[stuid].studentID) {
                                    //先比较不可修改的分数
                                    for (let tempcount = 0; tempcount < count; tempcount++) {
                                        if (JSONscore.participatesList[stuid]['score' + tempcount] !== value.participatesList[stuid]['score' + tempcount]) {
                                            Message.success('信息发生变化!')
                                            //发生变化changeList修改为true，进入修改操作
                                            this.state.changeList = true
                                            //只要遍历到有变化的地方，就可以停止遍历直接进行修改
                                            break;
                                        }
                                    }
                                    //再比较里面的学生信息
                                    if (JSONscore.participatesList[stuid].student.name !== value.participatesList[stuid].student.name) {
                                        Message.success('信息发生变化!')
                                        //发生变化changeList修改为true，进入修改操作
                                        this.state.changeList = true
                                        //只要遍历到有变化的地方，就可以停止遍历直接进行修改
                                        break;
                                    }
                                }
                            }
                        }
                        //如果为真，即修改数据
                        if (this.state.changeList) {
                            // 把新添加进来的
                            for (var i = 0; i < n; i++) {
                                value.participatesList[i]["showSave"] = false;
                                value.participatesList[i]["sum"] = 0;
                                for (var j = 0; j < m; j++) {
                                    for (var k = 0; k < p; k++) {
                                        if ((value.scoreitems[j]["id"] == value.scoresListNoExpert[k]["scoreItemID"]) &
                                            (value.participatesList[i]["id"] == value.scoresListNoExpert[k]["participantID"]) &
                                            (value.scoreitems[j]["byexpert"] == true)
                                        ) {
                                            value.participatesList[i]["score" + j] = value.scoresListNoExpert[k]["score"];
                                        }
                                    }
                                }
                            }

                            //用现有的每一个JSONscore.participatesList去匹配在value.participatesList最新的位置
                            for (let i = 0; i < JSONscore.participatesList.length; i++) {
                                //遍历value.participatesList，如果studentID和目前JSONscore.participatesList的studentID一样，说明位置被修改到j
                                for (let j = 0; j < value.participatesList.length; j++) {
                                    //首先判断studentID位置是否相等
                                    if (JSONscore.participatesList[i].studentID === value.participatesList[j].studentID) {
                                        // 如果小于count，说明还有不可修改项，不可修改项以后端为准
                                        for (let tempcount = 0; tempcount < count; tempcount++) {
                                            if (JSONscore.participatesList[i]['score' + tempcount] != value.participatesList[j]['score' + tempcount]) {
                                                JSONscore.participatesList[i]['score' + tempcount] = value.participatesList[j]['score' + tempcount]
                                            }
                                        }
                                        // 综合前端后端获取最新的分数,把所有的分赋值给value
                                        for (let key = count; key < JSONscore.scoreitems.length - 1; key++) {
                                            if (JSONscore.participatesList[i]['score' + key]) {
                                                value.participatesList[j]['score' + key] = JSONscore.participatesList[i]['score' + key]
                                            }
                                        }
                                        //计算总分
                                        let sum = 0
                                        for (let finishSum = 0; finishSum < value.scoreItemCount; finishSum++) {
                                            if (value.participatesList[j]['score' + finishSum]) {
                                                sum += value.participatesList[j]['score' + finishSum] * value.scoreitems[finishSum].coef
                                            }
                                        }
                                        //把总分也给过去
                                        value.participatesList[j].sum = sum
                                        //传递保存状态
                                        value.participatesList[j].showSave = JSONscore.participatesList[i].showSave
                                    }
                                }

                            }
                            // 将value提交到vuex中，此时页面保存的是最新的value(包含当前已经评的分)，通过页面信息提示刷新，页面获得新数据
                            context.commit('INIT_SCORE', value)
                            //修改完毕后把changeList置为false，应该可以不写
                            // changeList = false
                        } else {
                            //如果不为真，那在刷新时只要把JSONscore提交即可
                            context.commit('INIT_SCORE', JSONscore)
                        }
                    } else {
                        context.commit('INIT_SCORE', value)
                    }
                }
            })
                resolve()
            })
            return promise
        },
        setScoreParticipatesList(context, data) {
            context.commit('INIT_ScoreParticipatesList', data)
        },
        initchangeList(context) {
            context.commit('INIT_initchangeList')
        }

    },

})

store.watch(function (state) {
    return state.sessions
}, function (val) {
    localStorage.setItem('vue-chat-session', JSON.stringify(val));
}, {
    deep: true /*这个貌似是开启watch监测的判断,官方说明也比较模糊*/
})


export default store;