<template>
  <div>
   <AddActStep v-show="typeof $route.query.addActive !== 'undefined' " :active="parseInt($route.query.addActive)" :actID="keywords" :act-name="keywords_name"></AddActStep>
   <el-button icon="el-icon-s-custom" style="float: right;margin-top: 12px" type="primary" @click="change2GroupManage" v-show="$route.query.addActive == 5 && $route.query.mode == 'admin' ">
    组内人员管理
   </el-button>
   <div style="display: flex; justify-content: left">
    <div style="width: 100%;text-align: center;font-size: 20px" v-show="typeof $route.query.addActive === 'undefined'">专家管理</div>
    <div style="margin-left: auto;">
     <el-button icon="el-icon-back" type="primary" @click="back" style="float: right" v-show="typeof $route.query.addActive === 'undefined'">
      返回
     </el-button>
    </div>
   </div>
   <el-tabs v-show="!$route.query.flag" v-model="activeName" style="width: 70%" @tab-click="change2Par">
    <el-tab-pane label="选手管理" name="participant"></el-tab-pane>
    <el-tab-pane label="专家管理" name="expert"></el-tab-pane>
   </el-tabs>
    <div style="display: flex; justify-content: left">
<!--    {{ keywords_name }} <a v-show="flag===0">专家名单</a> <a v-show="flag==1">专家打分</a>-->
<!--      <div style="margin-left: auto">-->
<!--        <el-button icon="el-icon-back" type="primary" @click="back">-->
<!--          返回-->
<!--        </el-button>-->
<!--      </div>-->
    </div>
<!--    <div style="display: flex; justify-content: left;margin-top:10px">-->
<!--      <div v-if="flag==0">-->
<!--        <span  style="font-weight:600;">导入新数据</span> <a>第一步：</a>-->
<!--        <el-button-->
<!--            type="primary"-->
<!--            @click="exportMo"-->
<!--            icon="el-icon-upload"-->
<!--            style="margin-right: 8px"-->
<!--        >-->
<!--          下载模板-->
<!--        </el-button>-->
<!--        <a >第二步：</a>-->
<!--        <el-upload-->
<!--            :show-file-list="false"-->
<!--            :before-upload="beforeUpload"-->
<!--            :on-success="onSuccess"-->
<!--            :on-error="onError"-->
<!--            :disabled="importDataDisabled"-->
<!--            style="display: inline-flex; margin-right: 8px"-->
<!--            :action="UploadUrl()"-->
<!--        >-->
<!--          <el-button-->
<!--              :disabled="importDataDisabled"-->
<!--              type="primary"-->
<!--              :icon="importDataBtnIcon"-->
<!--          >-->
<!--            {{ importDataBtnText }}-->
<!--          </el-button>-->
<!--        </el-upload>-->
<!--      </div>-->

<!--    </div>-->
<!--    <div v-show="flag == 0"><br/>如果专家是本单位的，工号必须填，用户名和密码将被忽略；如果专家不为本单位的，工号不填，用户名和密码必须填。-->
<!--        <br/>如果数据库中已有该专家的记录，则将根据填写信息进行更新，用户名和密码不更新。-->
<!--    </div>-->
    <div>
      <el-button type="success" @click="showMethod" style="margin-top: 0px" v-if="haveGroup || groupID" v-show="!$route.query.flag ">
        添加专家
      </el-button>
     <el-tooltip class="item" effect="dark" content="当前活动无分组，无法添加专家。请先在分组管理中添加分组后再试。" placement="top-start" v-else :disabled='false'>
      <span>
      <el-button type="success" @click="showMethod" style="margin-top: 15px"  :disabled="true">
       添加专家
      </el-button>
      </span>
     </el-tooltip>

    </div>

    <div style="margin-top: 10px">
      <el-table
          ref="multipleTable"
          :data="hrs"
          stripe
          border
          v-loading="loading"
          @cell-dblclick="tabClick"
          :row-class-name="tableRowClassName"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%"
          @cell-mouse-enter="handleCellMouseEnter"
          @cell-mouse-leave="()=>{
            if(this.editing === false){
              this.tabClickIndex = -1;
              this.tabClickLabel = '';
            }
          }"
      >
        <el-table-column prop="name" fixed label="专家姓名" min-width="3%">
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex &&
                  tabClickLabel === '专家姓名' && flag==0
                "
                @input="editing = true"
                v-model.trim="scope.row.name"
                maxlength="20"
                size="mini"
                @focus="beforehandleEdit(scope.$index,scope.row,'name')"
                @change="handleEdit(scope.$index,scope.row,'name')"
                @blur="inputBlur"
            />

            <span class="spanscoped" v-else>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" fixed label="组名" min-width="3%">
              <template slot-scope="scope">
                  <span class="spanscoped">{{ scope.row.groupName }}</span>
              </template>
          </el-table-column>
        <el-table-column
            v-if="flag==0"
            prop="idnumber"
            label="证件号码"
            align="center"
            min-width="4%"
        >
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex &&
                  tabClickLabel === '证件号码'
                "
                @input="editing = true"
                v-model.trim="scope.row.idnumber"
                maxlength="18"
                size="mini"
                @focus="beforehandleEdit(scope.$index,scope.row,'idnumber')"
                @change="handleEdit(scope.$index,scope.row,'idnumber')"
                @blur="inputBlur"
            />

            <span class="spanscoped" v-else>{{ scope.row.idnumber }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="username"
            align="left"
            label="用户名"
            min-width="3%"
        >
          <template slot-scope="scope">
            <!--            <span v-if="scope.row.index === tabClickIndex && tabClickLabel === '专家姓名'">
              <el-input v-model="scope.row.username" size="mini" @blur="inputBlur"/>
            </span>
            <span v-else>{{ scope.row.username }}</span>-->
            <span>{{ scope.row.username }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="phone"
            align="center"
            label="电话"
            min-width="3%"
        >
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex && tabClickLabel === '电话' && flag==0
                "
                @input="editing = true"
                v-model.trim="scope.row.phone"
                maxlength="15"
                size="mini"
                @focus="beforehandleEdit(scope.$index,scope.row,'phone')"
                @change="handleEdit(scope.$index,scope.row,'phone')"
                @blur="inputBlur"
            />
            <span class="spanscoped" v-else>{{ scope.row.phone }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" align="left" min-width="3%">
          <template slot-scope="scope">
            <el-input
                v-if="
                  scope.row.index === tabClickIndex && tabClickLabel === '邮箱' && flag==0
                "
                @input="editing = true"
                v-model.trim="scope.row.email"
                size="mini"
                maxlength="25"
                @focus="beforehandleEdit(scope.$index,scope.row,'email')"
                @change="handleEdit(scope.$index,scope.row,'email')"
                @blur="inputBlur"
            />
            <span class="spanscoped" v-else>{{ scope.row.email }}</span>
          </template>
        </el-table-column>
        <el-table-column
            v-if="mode === 'admin' || mode==='adminSub' && flag == 0"
            prop="role"
            label="角色设置"
            align="center"
            min-width="3%"
        >
          <template slot-scope="scope">
            <el-select v-model="scope.row.role"
                       placeholder="请选择"
                       v-focus
                       @focus="beforehandleEdit(scope.$index,scope.row,'role')"
                       @change="changeRole(scope.$index,scope.row)"
                       @blur="inputBlur"
                       >
              <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
            v-if="!$route.query.addActive"
            align="center"
            min-width="3%"
            label="已提交评分"
            prop="finished"
        >
          <template slot-scope="scope">
            <span v-if="scope.row.finished == 0" style="color:red">否</span>
            <span v-else>是</span>
          </template>
        </el-table-column>
        <el-table-column align="center" min-width="10%" label="操作">
          <template slot-scope="scope">
            <el-button
                v-if="flag==0 && !$route.query.addActive"
                @click="reset_password(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-refresh-right"
                type="primary"
                plain
            >重置密码
            </el-button
            >
            <el-button
                @click="jumperInToS(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-refresh-right"
                type="primary"
                v-show="haveSub!=1 && !$route.query.addActive"
                plain
            >查看专家评分
            </el-button>
            <el-button
                @click="changeFinished(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-refresh-right"
                type="primary"
                :disabled="scope.row.finished==0"
                v-show="haveSub!=1 && !$route.query.addActive"
                plain
            >退回评分
            </el-button>
            <el-button
                @click="Delete_ExActivity(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-refresh-right"
                type="danger"
                plain
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin: 20px 0; display: flex; justify-content: left">
        <!-- <div>
          <el-button
            @click="toggleSelection()"
            type="primary"
            icon="el-icon-close"
            >取消选择</el-button
          >
        </div> -->
        <!--        <div style="margin-left: 8px">
          <el-button @click="handleAddDetails()" type="primary" icon="el-icon-plus"
          >新增</el-button
          >
        </div>-->
        <div style="margin-left: 8px">
          <!--          <el-button @click="save()" type="primary" icon="el-icon-check"
          >保存</el-button
          >-->
        </div>
<!--        <div style="margin-left: auto">-->
<!--          <el-pagination-->
<!--              v-show="mode==='admin'"-->
<!--              background-->
<!--              @current-change="currentChange"-->
<!--              @size-change="sizeChange"-->
<!--              layout="sizes, prev, pager, next, jumper, ->, total, slot"-->
<!--              :total="total"-->
<!--          >-->
<!--          </el-pagination>-->
<!--        </div>-->
      </div>

      <el-dialog :title="err_title "  :visible.sync="err_dialogVisible_edit" width="40%" >
        <div style="background-color: bisque; padding-left: 5px;padding-top: 5px;font-size: 20px;" v-for="i in err_msg">{{ i }}<hr>
        </div>
        <span slot="footer" class="dialog-footer" >
          <el-button @click="err_dialogVisible_edit = false" type="warning">关闭</el-button>
        </span>
      </el-dialog>

      <!--弹窗-->
      <el-dialog :title="title" :visible.sync="dialogVisible_edit" width="40%">
        <div>
          已经存在于其他组，是否需要调换？
          <el-table
              ref="multipleTable"
              :data="unsureinfo"
              stripe
              border
              v-loading="loading"
              @cell-click="tabClick"
              :row-class-name="tableRowClassName"
              element-loading-text="正在加载..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.08)"
              style="width: 100%"
          >
            <el-table-column prop="name" fixed label="专家姓名" min-width="5%">
              <template slot-scope="scope">
                <span>{{ scope.row.name }}</span>
              </template>
            </el-table-column>
            <el-table-column
                prop="idnumber"
                label="证件号码"
                align="left"
                min-width="5%"
            >
              <template slot-scope="scope">
                <span>{{ scope.row.idnumber }}</span>
              </template>
            </el-table-column>
            <el-table-column
                prop="oldgroupname"
                align="left"
                label="已经存在组名"
                min-width="5%">
              <template slot-scope="scope">
                <span>{{ scope.row.oldgroupname }}</span>
              </template>
            </el-table-column>
            <!--            <el-table-column
                          prop="username"
                          align="left"
                          label="用户名"
                          min-width="5%"
                        >
                          <template slot-scope="scope">
                            <span>{{ scope.row.username }}</span>
                          </template>
                        </el-table-column>
                        <el-table-column
                          prop="phone"
                          align="left"
                          label="电话"
                          min-width="5%"
                        >
                          <template slot-scope="scope">
                            <span>{{ scope.row.phone }}</span>
                          </template>
                        </el-table-column>-->
            <el-table-column
                prop="email"
                label="邮箱"
                align="left"
                min-width="3%">
              <template slot-scope="scope">
                <span>{{ scope.row.email }}</span>
              </template>
            </el-table-column>
            <el-table-column
                align="left"
                min-width="15%"
                label="操作">

              <template slot-scope="scope">

                <el-button @click="update(scope.row)" style="padding: 4px" size="mini" icon="el-icon-check"
                           type="primary"
                           plain>调换到本组：{{ keywords_name }}
                </el-button>

              </template>
            </el-table-column>
          </el-table>
        </div>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible_edit = false">关闭</el-button>
        </span>
      </el-dialog>
      <el-dialog :title="title" :visible.sync="dialogVisible_method" width="55%" center @close="handleClose">
        <el-tabs type="border-card">
          <el-tab-pane label="手动添加">
           <el-form class="registerContainer" ref="manualAddForm" :rules="manualAddRules" :model="manualAddForm">
             <el-form-item label="组别:" prop="groupID" v-show="!groupID">
               <el-select v-model="currentAddGroup" placeholder="请选择添加的组别"  @change="chooseGroup($event)" style="padding-left: 10px">
                 <el-option
                     v-for="x in groups"
                     :key="x.name"
                     :label="x.name"
                     :value="x.id">
                 </el-option>
               </el-select>
             </el-form-item>
            <el-form-item label="身份证号:" prop="idnumber">
             <el-input style="width: 60%"  v-model="manualAddForm.idnumber" @blur="getInfoByIDNumber();fillPassword()"></el-input>
            </el-form-item>
            <el-form-item label="姓名:" prop="name">
             <el-input style="width: 60%" v-model="manualAddForm.name" :disabled="manualAddFormDisabled"></el-input>
            </el-form-item>
            <el-form-item label="工号:" prop="jobNumber" >
             <el-input style="width: 60%" v-model="manualAddForm.jobNumber" :disabled="manualAddFormDisabled" @blur="fillUserName"></el-input>
            </el-form-item>
            <el-form-item label="电话:" prop="phone">
             <el-input style="width: 60%" v-model="manualAddForm.phone" :disabled="manualAddFormDisabled"></el-input>
            </el-form-item>
            <el-form-item label="邮箱:" prop="email">
             <el-input style="width: 60%" v-model="manualAddForm.email" :disabled="manualAddFormDisabled"></el-input>
            </el-form-item>
            <el-form-item label="性别:" prop="sex">
             <el-input style="width: 60%" v-model="manualAddForm.sex" :disabled="manualAddFormDisabled"></el-input>
            </el-form-item>
            <el-form-item label="用户名:" prop="username" >
             <el-input style="width: 60%" v-model="manualAddForm.username" @input="$forceUpdate()" :disabled="manualAddFormDisabled"></el-input>
            </el-form-item>
            <el-form-item label="密码:" prop="password">
             <el-input style="width: 60%" v-model="manualAddForm.password" @input="$forceUpdate()" :disabled="manualAddFormDisabled"></el-input>
            </el-form-item>
           </el-form>
           <el-button type="primary" @click="manualAdd">添加</el-button>
          </el-tab-pane>
          <el-tab-pane label="从本单位添加">
           <div style="display: flex; justify-content: left">
             <el-select v-model="currentAddGroup" placeholder="请选择添加的组别"  @change="chooseGroup($event)" style="padding-right: 10px" v-show="!groupID">
               <el-option
                   v-for="x in groups"
                   :key="x.name"
                   :label="x.name"
                   :value="x.id">
               </el-option>
             </el-select>
            <el-input
                v-model="searchText"
                placeholder="请输入工号或姓名进行搜索"
                @keyup.enter.native="search"
                @input="search"
            >
             <template #append>
              <el-button icon="el-icon-search" type="success" @click="search"></el-button>
             </template>
            </el-input>
           </div>
            <el-table
                ref="multipleTable"
                :data="currentExpert"
                tooltip-effect="dark"
                style="width: 100%"
                @selection-change="handleSelectionChange"
                :row-key="getRowKeys">
              <el-table-column
                  type="selection"
                  :reserve-selection="true"
                  :selectable="checkSecletion"
                  width="40px">
              </el-table-column>
             <el-table-column
                 prop="jobNumber"
                 label="工号"
                 show-overflow-tooltip>
             </el-table-column>
              <el-table-column
                  prop="name"
                  label="姓名">
              </el-table-column>
            </el-table>
            <div class="block" style="padding-top: 10px">
              <el-pagination
                  @current-change="currentChange"
                  @size-change="sizeChange"
                  :current-page="page"
                  layout="sizes, prev, pager, next, jumper, ->, total, slot"
                  :total="total">
              </el-pagination>
            </div>
            <div style="color: #4b8ffe ;float: right">
              已选择{{multipleSelection.length}}位专家
              <el-button type="primary" @click="add" style="padding-left: 10px">
                添加
              </el-button>
            </div>
          </el-tab-pane>
          <el-tab-pane label="批量导入">
            <div style="display: flex; justify-content: left;margin-top:10px">
              <div v-if="flag==0">
                <div v-show="flag == 0">如果专家是本单位的，工号必须填，用户名和密码将被忽略；如果专家不为本单位的，工号不填，用户名和密码必须填。
                  <br/>如果数据库中已有该专家的记录，则将根据填写信息进行更新，用户名和密码不更新。
                </div><br/>
                <span  style="font-weight:600;">导入新数据</span> <a>第一步：</a>
                    <el-button
                        type="primary"
                        @click="exportMo"
                        icon="el-icon-upload"
                        style="margin-right: 8px"
                    >
                      下载模板
                    </el-button>
                <a >第二步：</a>
                <el-upload
                    :show-file-list="false"
                    :before-upload="beforeUpload"
                    :on-success="onSuccess"
                    :on-error="onError"
                    :disabled="importDataDisabled"
                    style="display: inline-flex; margin-right: 8px"
                    :action="UploadUrl()"
                >
                  <el-button
                      :disabled="importDataDisabled"
                      type="primary"
                      :icon="importDataBtnIcon"
                  >
                    {{ importDataBtnText }}
                  </el-button>
                </el-upload>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="从大组添加" v-if="mode==='secretarySub'">
            <el-table
                ref="multipleTable"
                :data="parentGroup"
                tooltip-effect="dark"
                style="width: 100%"
                @selection-change="handleSelectionChange"
                :row-key="getRowKeys">
              <el-table-column
                  :reserve-selection="true"
                  :selectable="checkSecletion"
                  type="selection"
                  width="40px">
              </el-table-column>
             <el-table-column
                 prop="jobNumber"
                 label="工号"
                 show-overflow-tooltip>
             </el-table-column>
              <el-table-column
                  prop="name"
                  label="姓名"
              >
              </el-table-column>
            </el-table>
            <el-button type="primary" @click="add" style="float: right;margin-top: 10px">
              添加
            </el-button>
          </el-tab-pane>
        </el-tabs>

      </el-dialog>
    </div>

  </div>
</template>

<script>
import {Message} from 'element-ui'
import {validateInputIdCard,checkIdCard} from "@/utils/check";
import sha1 from "sha1";
import PinYinMatch from 'pinyin-match';
import AddActStep from "@/components/AddActStep.vue";
import {log} from "@/utils/sockjs";
import axios from "axios";

export default {
  name: "SalSobCfg",
 components: {AddActStep},
  data() {
    return {
     activeName: 'expert',
     searchText: '',
     changePasswordFlag:false,
     changeUserNameFlag:false,
     manualAddFormDisabled:false,
     manualAddRules:{
      idnumber:[
       { validator: validateInputIdCard, trigger: "blur" }],
     },
     manualAddForm:{
      name: '',
      phone: '',
      idnumber: '',
      email:'',
      sex:'',
      jobNumber:'',
      username:'',
      password:''
     },
      editing: false,
      activityID:-1,
      flag:0,
      haveSub:0,
      haveGroup:false,
      groups:[],
      currentAddGroup:'',
      //当前焦点数据
      currentfocusdata: "",
      currentrole:"",
      searchValue: {
        compnayName: null,
      },
      title: "",
      importDataBtnText: "导入专家",
      importDataBtnIcon: "el-icon-plus",
      importDataDisabled: false,
      page: 1,
      tabClickIndex: null, // 点击的单元格
      tabClickLabel: "", // 当前点击的列名
      keywords: "",
      activitydata: [],
      keywords_name: "",
      ACNAME:"",
      groupID: '',
      groupIDParent:'',
      size: 10,
      total: 0,
      loading: false,
      hrs: [],
      experts: [],
      experts_raw: [],
      currentExpert:[],
      multipleSelection:[],
      currentExperts: [],
      parentGroup:[],
      unsureinfo: [],
      dialogVisible_edit: false,
      dialogVisible_method: false,
      selectedRoles: [],
      allroles: [],
      options: [{
        value: '专家',
        label: '专家'
      }, {
        value: '秘书',
        label: '秘书'
      }, {
        value: '组长',
        label: '组长'
      }],
      value: '',
      hr_info: {
        id: null,
        compnayName: null,
        institutionID: null,
        name: "javaboy",
        phone: "18568128889",
        email: "123@126.com",
        enabled: 1,
        username: "test123",
        password: "123",
        role: 1,
        comment: null,
      },
      hr_info_new: {
        id: null,
        compnayName: null,
        institutionID: null,
        name: "javaboy",
        phone: "18568128889",
        email: "123@126.com",
        enabled: 1,
        username: "test123",
        password: "123",
        role: 1,
        comment: null,
      },
      rules: {
        compnayName: {
          required: true,
          message: "请输入单位名称",
          trigger: "blur",
        },
        institutionID: {
          required: true,
          message: "请输入单位编号",
          trigger: "blur",
        },
        name: {required: true, message: "请输入用户名", trigger: "blur"},
        phone: {required: true, message: "请输入电话", trigger: "blur"},
        role: {required: true, message: "请输入角色", trigger: "blur"},
        email: {required: true, message: "请输入邮箱", trigger: "blur"},
        enabled: {required: true, message: "请输入enable", trigger: "blur"},
        username: {
          required: true,
          message: "请输入username",
          trigger: "blur",
        },
        password: {required: true, message: "请输入密码", trigger: "blur"},
        comment: {required: false, message: "请输入备注", trigger: "blur"},
      },

      err_dialogVisible_edit: false,
      err_title: '',
      err_msg: '',
      mode:"",
    };
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user')); //object信息
    },
  },
  created() {
    //this.keywords = this.$route.query.keywords;
  },
  mounted() {
    this.keywords = this.$route.query.keywords;
    this.activityID = this.$route.query.activityID;
    this.keywords_name = this.$route.query.keyword_name;
    this.groupID = this.$route.query.groupID;
    this.groupIDParent = this.$route.query.groupIDParent;
    this.ACNAME = this.$route.query.keywords_name;
    this.mode = this.$route.query.mode;
    this.haveSub = this.$route.query.haveSub;
    this.flag = this.$route.query.flag == 1 ? 1 : 0;
    this.initHrs();
  },
  methods: {
   change2GroupManage(){
    const _this = this;
    _this.$router.push({
     path: "/ActivitM/table",
     query: {
      keywords: this.$route.query.keywords,
      keyword_name: this.$route.query.keyword_name,
      mode:this.mode,
      addActive:this.$route.query.addActive,
      haveSub: this.$route.query.haveSub,
     }
    });
   },
   change2Par(){
    this.change()
   },
   getRowKeys(row) {
    return row.jobNumber;
   },
   search() {
    if (this.searchText === ''){
     this.experts = this.experts_raw
    }else if (/^\d+$/.test(this.searchText)){ // 纯数字，按工号搜索
     this.experts = this.experts_raw.filter(item => item.jobNumber != null && item.jobNumber.includes(this.searchText))
    }else if (/^[a-zA-Z]*$/.test(this.searchText)){ //纯英文，考虑首字母
      this.experts = this.experts_raw.filter(item => PinYinMatch.match(item.name,this.searchText))
    } else { // 非纯数字，按姓名搜索
     this.experts = this.experts_raw.filter(item => item.name.includes(this.searchText))
    }
    this.total = this.experts.length
    this.getCurrentExperts()
    this.page = 1
   },
   handleClose(){
    this.$refs.manualAddForm.resetFields();
    this.manualAddFormDisabled=false;
    this.changeUserNameFlag=false;
    this.changePasswordFlag=false;
    this.searchText = ''
    this.multipleSelection = []
    this.experts = this.experts_raw
    this.total = this.experts.length
    this.page = 1
    this.currentAddGroup=''
    this.$refs.multipleTable.clearSelection()
   },
   fillUserName(){
    if (this.changeUserNameFlag === false){
     this.manualAddForm.username = this.manualAddForm.jobNumber
     this.$forceUpdate()
    }
   },
   fillPassword(){
    if (this.changePasswordFlag === false && checkIdCard(this.manualAddForm.idnumber)){
     this.manualAddForm.password = this.manualAddForm.idnumber.substr(-6)
    }
   },
   getInfoByIDNumber(){
    if (this.manualAddFormDisabled === true){
     this.manualAddForm = {idnumber: this.manualAddForm.idnumber}
    }
    this.manualAddFormDisabled = false
    if(checkIdCard(this.manualAddForm.idnumber)){
     this.getRequest("/system/Experts/getByIDNumber?idNumber="+this.manualAddForm.idnumber).then(resp => {
      if (resp && resp.obj != null){
       this.manualAddForm = {
        name: resp.obj.name,
        phone: resp.obj.phone,
        idnumber: resp.obj.idnumber,
        email:resp.obj.email,
        sex:resp.obj.sex,
        jobNumber:resp.obj.jobNumber,
        username:resp.obj.username,
        password:'*************'
       }
       this.manualAddFormDisabled = true
      }
     })
    }
   },
   checkLeader(){
      for (let i=0; i<this.hrs.length; i++){
        if (this.hrs[i].role === "组长"){
        return true
        }
      }
      return false
   },
   manualAdd(){
    {
     this.manualAddForm.institutionid = this.user.institutionID;
     this.manualAddForm.activityID = this.keywords
     this.manualAddForm.groupID = this.groupID ? this.groupID : this.currentAddGroup
     if (!this.manualAddForm.groupID){
      this.$message({
       type: 'warning',
       message: '请选择小组!'
      })
      return
     }
     this.$refs['manualAddForm'].validate((valid) => {
      if (valid) {
       if (this.manualAddFormDisabled){ // teacher表中已经存在该专家，调用“从本单位添加的接口”
        let form = []
        form.push(this.manualAddForm)
        this.postRequest("/systemM/Experts/addExperts?groupID=" + this.manualAddForm.groupID + "&activityID=" + this.keywords, form).then(resp => {
         if (resp) {
          if (this.$route.query.groupID)
           this.initHrs(true,this.hrs.length);
          else{
           this.initHrs(true,this.hrs.length,this.manualAddForm.groupID);
          }
          // this.$message({
          //  type: 'success',
          //  message: '添加成功!'
          // })
          this.dialogVisible_method = false
         }
        })
       }else {
        this.manualAddForm.updateUserName = true
        this.manualAddForm.password = sha1(this.manualAddForm.password)
        this.postRequest1("/systemM/Experts/manualAdd",this.manualAddForm).then(resp => { // teacher表中无该专家
         if (resp && resp.status === 200) {
          // this.$message({
          //  message: '添加成功!',
          //  type: 'success'
          // });
          if (this.$route.query.groupID)
           this.initHrs(true,this.hrs.length);
          else{
           this.initHrs(true,this.hrs.length,this.manualAddForm.groupID);
          }
          this.initHrs(true);
          this.dialogVisible_method = false
         }
        });
       }

      }
     })
    }

   },
    Delete_ExActivity(si) {
      if (si.finished)
      {
        this.$message({
          type: "info",
          message: "无法删除评分已提交的专家",
        });
        return;
      }
      this.$confirm("此操作将永久删除【" + si.name + "】, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            si.institutionid=this.user.institutionID;
            if (si.groupID === null)
              si.groupID = -1
            this.postRequest("/systemM/Experts/delete?groupid=" + si.groupID + "&activityid=" + this.keywords, si).then(resp => {
              if (resp) {
                this.$message({
                  type: "success",
                  message: "删除成功",
                });
                this.initHrs();
              }
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
    },
    enabledChange(hr) {
      //delete hr.roles;
      this.putRequest("/system/admin/", hr).then((resp) => {
        if (resp) {
          this.initHrs();
        }
      });
    },
    initAllRoles() {
     this.loading = true
      this.getRequest("/system/hr/roles").then((resp) => {
        if (resp) {
          this.allroles = resp;
        }
      });
    },
    initHrs(checkFlag,oldLen,checkGroupID) {
      this.loading = true;
      if (typeof this.activityID == "undefined" || this.mode === 'secretary') { // 此时是从分组管理进入的
          this.getRequest(
              "/systemM/Experts/?keywords=" + this.groupID +
              "&page=" + 1 +
              "&size=" + 1000 // 避免分页
          ).then((resp) => {
            this.loading = false;
              if (resp) {
                  this.hrs = resp;
               if (typeof oldLen != "undefined" && this.hrs.length > oldLen) {
                 this.$message({
                  type: "success",
                  message: "添加成功",
                 });
                }else if (typeof oldLen != "undefined"){
                this.$message({
                 type: "warning",
                 message: "该专家已存在，无需重复添加！",
                });
               }
               if (typeof checkFlag != "undefined" && checkFlag === true){
                 if (!this.checkLeader() && this.hrs.length > 0){ // 无组长，指定第一个为组长
                  this.setLeader(this.groupID,this.hrs[0].id)
                 }
               }
              }
          });
      }else {  // 从活动或子活动进入
          if (this.mode === 'admin' || this.mode === 'adminSub'){ // 直接基于活动ID返回所有专家
              this.getRequest(
                  "/systemM/Experts/allByActID?activityID=" + this.activityID
              ).then((resp) => {
                  if (resp) {
                   this.loading = false;
                   this.hrs = resp.obj
                   if (typeof oldLen != "undefined" && this.hrs.length > oldLen) {
                    this.$message({
                     type: "success",
                     message: "添加成功",
                    });
                   }
                   if (checkFlag && checkFlag === true && checkGroupID){
                    console.log("check")
                    this.checkAndSetLeader(checkGroupID)
                   }
                  }
              });
          }
      }
      this.initExperts();
      if (this.mode==='admin'&&!this.groupID)
        this.checkHaveGroup();
      if (this.mode==='secretarySub')
        this.initParentGroup();
    },
   checkAndSetLeader(groupID){
    this.postRequest1("/systemM/Experts/checkAndSetLeader?groupID=" + groupID).then(resp => {
     if (resp && resp.status === 200 && resp.msg !== '') {
      this.$message({
       message: '已经设置'+resp.msg+'为组长',
       type: 'info'
      });
      this.initHrs();
     }
    });
   },
   setLeader(groupID,teacherID){
    if (!this.checkLeader() && this.hrs.length > 0){ // 无组长，指定第一个为组长
     this.postRequest1("/systemM/Experts/setLeader?groupID=" + groupID + "&teacherID=" + teacherID).then(resp => {
      if (resp && resp.status === 200) {
       this.$message({
        message: '已经设置'+this.hrs[0].name+'为组长',
        type: 'info'
       });
       this.initHrs();
      }
     });
    }
   },
      initExperts(){
      let url = '/system/Experts/getByInstitutionID/?institutionID=' + this.user.institutionID;
      this.getRequest(url).then(resp => {
        if (resp) {
          this.experts = resp.obj;
          this.experts_raw = resp.obj
          this.total = this.experts.length;
        }
      });
    },
    initParentGroup(){
      this.getRequest(
          "/systemM/Experts/?keywords=" + (this.groupIDParent ? this.groupIDParent : this.groupID) +
          "&page=" + 1 +
          "&size=" + 1000 // 避免分页
      ).then((resp) => {
        if (resp) {
          this.parentGroup = resp;
        }
      });
    },
    jumperInToS(data){
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/situation",
        query: {
          keywords: this.keywords,
          keyword_name: this.ACNAME,
          keywords_name:this.keywords_name,
          groupID: this.groupID ? this.groupID : data.groupID,
          expertID:data.id,
          expertName:data.name,
          institutionID:this.user.institutionID,
          isFinished:data.finished,
          mode:this.mode,
          flag:this.flag,
        },
      });
    },
    // UpdateCheckbox(data){
    //   this.getRequest("/secretary/setSecretary?teacherID=" + data.id + "&activityID=" + this.keywords + "&groupID=" + this.groupID + "&target=" + data.isSecretary)
    //       .then((resp) => {
    //         if(resp)
    //         {Message.success("更新成功");}
    //         else
    //         {Message.error("更新失败");}
    //       });
    // },

    changeRole(index,data){
      if (data.groupID === null)
      {
        Message.warning("请先为该专家分组后再设置角色！")
        return
      }
      this.currentrole = data.role;
        this.getRequest("/secretary/setRole?teacherID=" + data.id + "&activityID=" + this.keywords + "&groupID=" + data.groupID + "&role=" + this.currentrole)
            .then((resp) => {
              if(resp)
              {Message.success("更新成功");}
              else
              {Message.error("更新失败");}
            });
    },
    advancedSearch() {
      this.getRequest(
          "/groups/basic/advanced/?keywords=" +
          this.keywords +
          "&keywords_name=" +
          this.keywords_name +
          "&page=" +
          1 +
          "&size=" +
          1000
      ).then((resp) => {
        if (resp) {
          this.hrs = resp.data;
        }
      });
    },
    checkSecletion(row,index){
      for (let i = 0; i < this.hrs.length; i++){
        if (row.idnumber === this.hrs[i].idnumber)
          return false;
      }
      return true;
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.getCurrentExperts();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.getCurrentExperts();
    },
    getCurrentExperts(){
      let begin = (this.page - 1) * this.size;
      let end = this.page * this.size;
      this.currentExpert = this.experts.slice(begin, end);
      this.$nextTick(() => {
        this.currentExpert.forEach(item => {
          for (let i = 0; i < this.hrs.length; i++){
            if (item.idnumber === this.hrs[i].idnumber)
              this.$refs.multipleTable.toggleRowSelection(item, true)
          }
        })
      })
    },
    //取消表格选择
    // toggleSelection(rows) {
    //   if (rows) {
    //     rows.forEach((row) => {
    //       this.$refs.multipleTable.toggleRowSelection(row);
    //     });
    //   } else {
    //     this.$refs.multipleTable.clearSelection();
    //   }
    // },
    handleAddDetails() {
      if (this.hrs === undefined) {
        this.hrs = new Array();
      }
      let obj = {};
      obj.activityID = this.keywords;
      obj.expertCount = 0;
      obj.participantCount = 0;
      this.hrs.push(obj);
    },
    back() {
      const _this = this;
     // if (typeof this.$route.query.addActive !== 'undefined'){
     //  _this.$router.push({
     //   path: "/ActivitM/table",
     //   query: {
     //    keywords: this.$route.query.activityID,
     //    keyword_name: this.$route.query.actName,
     //    mode:this.mode,
     //    addActive:this.$route.query.addActive,
     //    haveSub:this.$route.query.haveSub,
     //   },
     //  });
     //  return
     // }
      if (this.mode === "admin"){
          // console.log(this.activityID)
          if (typeof this.activityID !== "undefined") { // 从主活动进入
              _this.$router.push({
                  path: "/ActivitM/search",
              });
              return
          }
        _this.$router.push({
          path: this.flag ? "/ActivitM/score" : "/ActivitM/table",
          query: {
            keywords: this.keywords,
            keyword_name: this.ACNAME,
            mode:this.mode,
          },
        });
      }
      else if (this.mode === "secretary"){
        _this.$router.push({
          path: '/secretary/ActManage',
          query: {
            keywords: this.keywords,
            keyword_name: this.ACNAME,
            groupID:this.groupID,
            mode:this.mode,
          },
        });
      } // ActivitM/score?keywords=50&keyword_name=人员管理开发专用-子活动1&mode=adminSub&backID=49
          // ActivitM/sobcfg?keywords=50&keyword_name=人员管理子活动1.1&keywords_name=人员管理开发专用-子活动1&groupID=49&mode=adminSub&flag=1
      else if (this.mode === "adminSub"){
          // _this.$router.push({
          //     path: "/ActivitM/score",
          //     query: {
          //         keywords:this.$route.query.keywords,
          //         keyword_name:this.$route.query.keyword_name,
          //         mode:this.$route.query.mode,
          //         backID:49,
          //         id: this.$route.query.backID,
          //     },
          // });
       this.$router.go(-1);
      }else{
       const _this = this
       this.$router.go(-1);
       // _this.$router.push({
       //  path: '/participantsM',
       //  query:{
       //   activityIDParent: this.$route.query.activityIDParent,
       //   activityID: this.keywords,
       //   groupIDParent: this.$route.query.groupIDParent,
       //   groupID: this.$route.query.groupID,
       //   actName: this.$route.query.actName,
       //   groupName: this.$route.query.groupName,
       //   isGroup: this.$route.query.isGroup,
       //   haveSub: this.$route.query.haveSub,
       //   id: this.$route.query.id,
       //   keywords: this.keywords,
       //   keyword_name: typeof this.keyword_name === 'undefined' ? this.$route.query.keyword_name : this.keyword_name,
       //   ACNAME:typeof this.keyword_name === 'undefined' ? this.$route.query.keyword_name : this.keyword_name,
       //   mode:this.mode,
       //   backID:this.$route.query.groupID,
       //   backActName:this.$route.query.backActName,
       //   smallGroup:this.$route.query.smallGroup,
       //   back:1,
       //  }
       // })
      }
    },
    tableRowClassName({row, rowIndex}) {
      // 把每一行的索引放进row
      row.index = rowIndex;
    },
    handleCellMouseEnter(row, column, cell, event) {
      if (this.editing === true)
        return;
      if (this.mode!=="secretary" && this.mode!=='secretarySub'){
        switch (column.label) {
          case "专家姓名":
            this.tabClickIndex = row.index;
            this.tabClickLabel = column.label;
            break;
          case "证件号码":
            this.tabClickIndex = row.index;
            this.tabClickLabel = column.label;
            break;
          case "邮箱":
            this.tabClickIndex = row.index;
            this.tabClickLabel = column.label;
            break;
          case "部门":
            this.tabClickIndex = row.index;
            this.tabClickLabel = column.label;
            break;
          case "电话":
            this.tabClickIndex = row.index;
            this.tabClickLabel = column.label;
            break;
          default:
            return;
        }
      }
    },
    // 添加明细原因 row 当前行 column 当前列
    tabClick(row, column, cell, event) {
      switch (column.label) {
        case "专家姓名":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        case "证件号码":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        case "邮箱":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        case "部门":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        case "电话":
          this.tabClickIndex = row.index;
          this.tabClickLabel = column.label;
          break;
        default:
          return;
      }
    },

    showParticipantsM(data) {
      const _this = this;
      _this.$router.push({
        path: "/participantsM",
        query: {
          keywords: data.id,
          keyword_name: data.name,
        },
      });
    },
    beforehandleEdit(index, row, label) {
      if(label === 'role') {
        this.currentrole = row.role
      }
      else {
        this.currentfocusdata = row[label]
      }
    },
    handleEdit(index, row, label) {
      if (row[label] === '') {
        Message.warning('输入内容不能为空！')
        row[label] = this.currentfocusdata
      } else {
        this.save(row)
        this.editing = false
      }
    },
    // 失去焦点初始化
    inputBlur() {
      this.tabClickIndex = null;
      this.tabClickLabel = "";
      this.currentfocusdata = "";
      this.currentrole = "";
    },
    save(si) {
      si.institutionid=this.user.institutionID;
      const _this = this;
      //console.log(si);
      //this.$router.push({name:'/scoreItem/basic/update',params:{scoreItem:_this.hrs,total:_this.total}})
      this.postRequest("/systemM/Experts/update", si).then((resp) => {
        Message.success(resp)
      });
    },
    update(si) {
      si.institutionid=this.user.institutionID;
      const _this = this
      //console.log(si);
      //this.$router.push({name:'/scoreItem/basic/update',params:{scoreItem:_this.hrs,total:_this.total}})
      this.postRequest("/systemM/Experts/updateGroupId?groupid=" + this.groupID + "&activityid=" + this.keywords, si).then(resp => {
      })
    },
    initData() {
      this.getRequest("/activities/basic/get_activity_info").then((resp) => {
        if (resp) {
          this.activitydata = resp;
        }
      });
    },
    onError(err, file, fileList) {
      this.importDataBtnText = "导入专家";
      this.importDataBtnIcon = "el-icon-plus";
      this.importDataDisabled = false;
    },
    onSuccess(res) {
      // console.log("res", res);
     this.loading = false

      if (res.msg === 'file error') {
        Message.error("文件内容或者格式有误，请不要修改表头，信息按格式填写！")
      } else if (res.msg === 'success') {
        Message.success('数据导入成功！')
      } else {
        this.err_dialogVisible_edit = true;
        this.err_msg = res.obj;
        this.err_title = "以下专家用户名重复：";
        Message.error("上传失败！",res.msg)
      }
      this.importDataBtnText = "导入数据";
      this.importDataBtnIcon = "el-icon-plus";
      this.importDataDisabled = false;
      this.initHrs(true);
    },
    beforeUpload() {
      this.importDataBtnText = "正在导入";
      this.importDataBtnIcon = "el-icon-loading";
      this.importDataDisabled = true;
    },
    UploadUrl() {
      if (typeof this.keywords !== "undefined") { // 从主活动进入
        return "/systemM/Experts/importWithGroupName?activityid=" + this.keywords+"&insititutionID="+this.user.institutionID;
      }else{
        return "/systemM/Experts/import?groupid=" + this.groupID + "&activityid=" + this.keywords+"&insititutionID="+this.user.institutionID;
      }
    },
    exportMo() {
      Message.success("正在下载模板");
      let url =''
        if (typeof this.keywords !== "undefined") {
           // window.open("/participants/basic/exportMoWithGroupName", "_parent");
          url = "/participants/basic/exportMoWithGroupName"
        }else{
          //  window.open("/participants/basic/exportMo", "_parent");
          url = "/participants/basic/exportMo"
        }
      axios({
        url: url,
        method: 'get',
        responseType: 'blob',
        headers: {
          'token': this.user.token ? this.user.token : ''
        }
      }).then((res) => {
        let url = window.URL.createObjectURL(new Blob([res]))
        let link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', 'expert'  + '.xls')
        document.body.appendChild(link);
        link.click();
      });
    },
    reset_password(row) {
      row.institutionid=this.user.institutionID;
      this.putRequest("/systemM/Experts/pass", row).then((resp) => {
        if (resp) {
          //弹窗
          if(resp==='已将密码设置为证件号码后6位!')
          {
            Message.success(resp);
          }
          else
          {
            Message.error(resp);
          }
        }
      });
    },
    change(){
      const _this = this
      if (this.groupID){
        _this.$router.push({
          path: '/participantsM',
          query:{
            activityIDParent: this.$route.query.activityIDParent,
            activityID: this.keywords ? this.keywords : this.$route.query.id,
            groupIDParent: this.$route.query.groupIDParent,
            groupID: this.$route.query.groupID,
            actName: this.$route.query.actName,
            groupName: this.$route.query.groupName,
            isGroup: this.$route.query.isGroup,
            haveSub: this.$route.query.haveSub,
            id: this.$route.query.id,
            keywords: this.keywords,
            keyword_name: typeof this.keyword_name === 'undefined' ? this.$route.query.keyword_name : this.keyword_name,
            ACNAME:typeof this.keyword_name === 'undefined' ? this.$route.query.keyword_name : this.keyword_name,
            mode:this.mode,
            backID:this.$route.query.groupID,
            backActName:this.$route.query.backActName,
            smallGroup:this.$route.query.smallGroup,
            addActive:this.$route.query.addActive,
            requireGroup:this.$route.query.requireGroup,
            forSecretary:this.$route.query.forSecretary,
          }
        })
      }
      else {
        _this.$router.push({
          path: '/ActivitM/group',
          query:{
            keywords: this.keywords,
            keyword_name: typeof this.keyword_name === 'undefined' ? this.$route.query.keyword_name : this.keyword_name,
            groupID:this.groupID,
            mode:this.mode,
            backID:this.activityID,
            addActive:this.$route.query.addActive,
          }
        })
      }
    },
    showMethod(){
      this.title='添加专家';
      this.dialogVisible_method=true;
      this.getCurrentExperts();
    },
    changeFinished(row){
      this.$confirm("是否确定退回打分", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
          .then(() => {
            this.postRequest("/systemM/Experts/withdraw?activityID=" + this.keywords + "&groupID=" + (this.groupID ? this.groupID : row.groupID) +"&expertID=" + row.id).then(resp => {
              if (resp) {
                this.initHrs();
                this.$message({
                  type: 'success',
                  message: '退回成功!'
                });
              }
            });
          })
    },
    chooseGroup(event){
      this.currentAddGroup=event;
    },
    add(){
      if (this.multipleSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择专家!'
        });
        return;
      }
      this.dialogVisible_method=false;
      const _this = this
      for (let i = 0; i < this.multipleSelection.length; i++) {
        if (this.multipleSelection[i].institutionid === null)
         this.multipleSelection[i].institutionid = this.multipleSelection[i].institutionID;
      }
      if (typeof this.groupID !== 'undefined'){
        this.currentAddGroup = this.groupID;
      }
      if (!this.currentAddGroup) {
         this.$message({
           type: 'warning',
           message: '请选择小组!'
         });
         return;
      }
     const groupID = this.currentAddGroup;
      this.postRequest("/systemM/Experts/addExperts?groupID=" + this.currentAddGroup + "&activityID=" + this.keywords, this.multipleSelection).then(resp => {
        if (resp) {
         // console.log("check")
          this.initHrs(true,this.hrs.length,groupID);
          // this.$message({
          //   type: 'success',
          //   message: '添加成功!'
          // });
        }
      })
    },
    handleSelectionChange(val){
      for(let i=0;i<val.length;i++){
        for (let j=0;j<this.hrs.length;j++){
          if (val[i].idnumber===this.hrs[j].idnumber){
            val.splice(i,1);
            i--;
            break;
          }
        }
      }
      this.multipleSelection=val;
    },
    checkHaveGroup() {
        this.getRequest('/activities/basic/checkHaveGroup?activityID='+this.keywords).then(res => {
          if (res.obj){
            this.haveGroup = true
            this.getRequest('/activities/basic/getAllGroup?activityID='+this.keywords).then(res => {
              if (res.obj){
                this.groups = res.obj;
              }
            })
          }
        })
    },
  },
};
</script>

<style>
.userinfo-container div {
  font-size: 12px;
  color: #409eff;
}

.userinfo-container {
  margin-top: 20px;
}

.img-container {
  width: 100%;
  display: flex;
  justify-content: center;
}

.userface-img {
  width: 72px;
  height: 72px;
  border-radius: 72px;
}

.hr-container {
  margin-top: 10px;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
}

.hr-card {
  width: 350px;
  margin-bottom: 20px;
}

.tb-edit.el-input {
  display: none;
}

.tb-edit.current-row.el-input {
  display: block;
}

.tb-edit.current-row.el-input + span {
  display: none;
}

.spanscoped {
  width: 100%;
  height: 100%;
  display: inline-block;
}

.el-pagination {
  text-align: center;
}

</style>
