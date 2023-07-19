<template>
  <div>
   <AddActStep v-show="typeof $route.query.addActive !== 'undefined'" :active="parseInt($route.query.addActive)" :actID="keywords" :actName="keywords_name"></AddActStep>
    <div>
      <div >
        <div style="display: flex; justify-content: left">
          <div style="width: 100%;text-align: center;font-size: 20px" v-show="typeof $route.query.addActive === 'undefined'">选手管理</div>
          <div style="margin-left: auto;float: right" v-show="typeof $route.query.addActive === 'undefined'">
            <el-button icon="el-icon-back" type="primary" @click="back" style="float: right" >
              返回
            </el-button>
          </div>
        </div>
        <el-tabs v-model="activeName" @tab-click="change2Exp" style="width: 70%">
          <el-tab-pane label="选手管理" name="participant"></el-tab-pane>
          <el-tab-pane label="专家管理" name="expert"></el-tab-pane>
          <div v-show="mode === 'secretary'">{{ keywords_name }}活动 选手名单<br/><br/></div>
        </el-tabs>
        <a>
          选手添加有三种模式：手动添加、从本单位添加、批量导入。<br/><br/>
        </a>
        <div style="display: flex;justify-content: space-between;">
          <!-- <div>
            <el-input placeholder="请输入单位名进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                      clearable
                      @clear="searchEmps"
                      style="width: 350px;margin-right: 10px" v-model="keyword"
                      @keydown.enter.native="searchEmps" :disabled="showAdvanceSearchView"></el-input>
            <el-button icon="el-icon-search" type="primary" @click="searchEmps" :disabled="showAdvanceSearchView">
              搜索
            </el-button>
          </div> -->
        </div>
      </div>
<!--      {{keywords_name}}活动<br/><br/>-->
<!--      选手导入可以分多次进行。<br/>-->
<!--      选手第一次导入时，可先不分组。此时可以将导入表格中的“分组名称”留空，进行导入操作。待分组后，再导入一次，从而实现分组。-->
<!--      选手的信息项以及评分项，也可在选手第一次导入时留空，待第二次、第三次（或之后）导入时填入那些信息。<br/>-->
      <div style="display: flex;justify-content: space-between;">

        <!-- <div>
          <el-input placeholder="请输入单位名进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                    clearable
                    @clear="searchEmps"
                    style="width: 350px;margin-right: 10px" v-model="keyword"
                    @keydown.enter.native="searchEmps" :disabled="showAdvanceSearchView"></el-input>
          <el-button icon="el-icon-search" type="primary" @click="searchEmps" :disabled="showAdvanceSearchView">
            搜索
          </el-button>
        </div> -->
        <div>
<!--          <span style="font-weight:600;">导入新数据</span> 第一步：-->
<!--          <el-button type="primary" @click="exportCheckbox" icon="el-icon-upload" style="margin-right: 8px">-->
<!--            下载模板-->
<!--          </el-button>-->
<!--          第二步：-->
<!--&lt;!&ndash;          导入选手按钮&ndash;&gt;-->
<!--          <el-upload-->
<!--              :show-file-list="false"-->
<!--              :before-upload="beforeUpload"-->
<!--              :on-success="onSuccess"-->
<!--              :on-error="onError"-->
<!--              :disabled="importDataDisabled"-->
<!--              style="display: inline-flex;"-->
<!--              action="#"-->
<!--              :http-request="handleChange">-->
<!--            <el-button :disabled="importDataDisabled" type="primary" :icon="importDataBtnIcon">-->
<!--              {{importDataBtnText}}-->
<!--            </el-button>-->
<!--          </el-upload>-->
          <el-button type="success" @click="showMethod">
            添加选手
          </el-button>
          <el-button type="primary" @click="groupsForParticipant" icon="el-icon-upload" style="margin-left: 10px">
            选手分组
          </el-button>
          <el-button type="danger" icon="el-icon-delete" @click="deleteGroupsOfParticipant"  style="margin-left: 10px">
            清空分组信息
          </el-button>
          <el-select
              style="margin-left: 8px;width: 100px;"
              v-model="filterParticipantsByState"
              placeholder="选手筛选"
              @change="filterParticipantsByStateMed"
          >
            <el-option
                v-for="item in filterParticipantsByStateList"
                :key="item"
                :label="item"
                :value="item">
            </el-option>
          </el-select>
        </div>
        <div>
          <el-button type="primary" @click="exportData" :loading="loading" icon="el-icon-download">
            导出专家打分
          </el-button>
          <el-button
              icon="el-icon-refresh"
              type="primary"
              @click="refreshact()">刷新</el-button>
<!--          <el-button icon="el-icon-back" type="primary" @click="back">-->
<!--            返回-->
<!--          </el-button>-->
        </div>
      </div>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="emps"
          :model="emps"
          stripe
          border
          v-loading="loading"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.12)"
          style="width: 100%">
        <el-table-column
            type="selection"
            min-width="3%">
        </el-table-column>
        <el-table-column
            prop="oldgroupname"
            fixed
            align="left"
            label="组名"
            width="100px">
        </el-table-column>
        <el-table-column
            prop="displaySequence"
            fixed
            align="left"
            label="序号"
            width="50px">
        </el-table-column>
        <el-table-column
            prop="code"
            align="left"
            label="编号"
            width="200px">
        </el-table-column>
        <el-table-column
            prop="idnumber"
            align="left"
            label="身份证号码"
            width="200px">
        </el-table-column>
        <el-table-column
            prop="name"
            align="left"
            label="姓名"
            width="100px">
        </el-table-column>
        <el-table-column
            prop="score"
            label="得分"
            align="center"
            width="80px">
        </el-table-column>
        <el-table-column align="left" min-width="10%" label="操作">
          <template slot-scope="scope">
            <el-button
                @click="showEditEmpView(scope.row)"
                style="padding: 4px"
                size="mini"
                icon="el-icon-collection"
                type="primary"
                plain
            >编辑</el-button
            >
            <el-button
                @click="Delete(scope.row)"
                style="padding: 4px"
                size="mini"
                type="danger"
                icon="el-icon-delete"
                plain
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="display: flex;justify-content: flex-end;margin:10px 0">
        <el-pagination
            background
            @current-change="currentChange"
            @size-change="sizeChange"
            layout="sizes, prev, pager, next, jumper, ->, total, slot"
            :total="total">
        </el-pagination>
      </div>
    </div>


    <el-dialog title="选手分组" :visible.sync="dialogPartipicantGroups" width="81%" center
               @close='closeDialogGroupOfParticipant'>
      <div>
        <div>
          <span>请选择分组依据：</span>
          <el-select
              style="margin-right: 20px;width: 150px;"
              v-model="selectedGroupInfo"
          >
            <el-option
                v-for="(item,key,index) in groupInfoNums"
                :key="key"
                :label="key"
                :value="key">
            </el-option>
          </el-select>

          <template v-if="groupSubOfSelectedInfos.length > 1">
            <span>请选择{{selectedGroupInfo}}：</span>
            <el-select
                style="margin-right: 20px;width: 250px;"
                v-model="selectedSubGroupInfo"
                multiple
            >
              <el-option
                  v-for="item in groupSubOfSelectedInfos"
                  v-if="item != 'infoItemID'"
                  :key="item"
                  :label="item + '（' + groupInfoNums[selectedGroupInfo][item].length +'）人'"
                  :value="item">
              </el-option>
            </el-select>
          </template>
          <br>

          <div style="margin-top: 10px">
            <template v-if="selectedGroupInfo != ''">
              <span>请选择排序依据：</span>
              <el-select
                  style="margin-right: 20px;width: 150px;"
                  v-model="sortBy"
              >
                <el-option
                    v-for="item in sortByList"
                    :key="item.id "
                    :label="item.name"
                    :value="item.name">
                </el-option>
              </el-select>
            </template>

            <template v-if="sortBy != ''">
              <span>请选择分组个数：</span>
              <el-select
                  style="margin-right: 20px;width: 150px;"
                  v-model="selectedGroupNums"
              >
                <el-option
                    v-for="item in groupNums"
                    :key="item"
                    :label="item"
                    :value="item">
                </el-option>
              </el-select>
            </template>
          </div>

          <div style="margin-top: 15px;width: 100%" >
            <template >
              <el-radio v-model="radio" label="1">均分每组人数</el-radio>
              <el-radio v-model="radio" label="2">指定每组人数</el-radio>
            </template>
            <el-button @click="creatGroup" type="primary">
              创建分组
            </el-button>
            <el-button @click="closeDialogGroupOfParticipant" type="primary">
              &nbsp;关&nbsp;&nbsp;闭&nbsp;
            </el-button>
          </div>

          <div id="tableOfGroupNums"
               class="inputOfGroupsBox"
               v-show="(selectedGroupInfo != ''
               // && selectedSubGroupInfo.length > 0 && groupSubOfSelectedInfos.length > 1) ||
               //          (groupSubOfSelectedInfos.length == 1 && selectedGroupInfo != ''))
                        // &&filterNoGroupPar.length >= selectedGroupNums
                        && selectedGroupNums > 0) ? true:false"
               >
            <span v-for="item in groupNumsInput" style="margin: auto;width: 100%;padding-bottom: 2px">
              第{{item.idx + 1}}组：
              <input v-model="item.value" style="width: 60px" :disabled="item.disabled"></input>
            </span>
          </div>
        </div>
      </div>
    </el-dialog>


    <el-dialog :title="title" :visible.sync="dialogVisible" width="30%" center>
      <el-form
          :label-position="labelPosition"
          label-width="100px"
          :model="emp"
          :rules="rules"
          ref="empForm"

      >
        <el-form-item label="显示顺序:" prop="name">
          <el-input
              size="mini"
              style="width: 200px"
              prefix-icon="el-icon-edit"
              v-model="emp.displaySequence"
              disabled
              placeholder="显示顺序"
          ></el-input>
        </el-form-item>
        <el-form-item label="选手姓名:" prop="name">
          <el-input
              size="mini"
              style="width: 200px"
              prefix-icon="el-icon-edit"
              v-model="emp.name"
              placeholder="请输入选手姓名"
          ></el-input>
        </el-form-item>
        <el-form-item label="证件号码:" prop="idnumber">
          <el-input
              size="mini"
              style="width: 200px"
              prefix-icon="el-icon-edit"
              v-model="emp.idnumber"
              disabled
              placeholder="证件号码"
          ></el-input>
        </el-form-item>
        <el-form-item
            v-for="(value,idx) in emp.scoremap"
            :key="idx"
            :label="value.name"
            width="180px"
        >
          <el-input
              placeholder="请打分"
              v-model.trim="value.score"
              clearable
          >
          </el-input>
        </el-form-item>
        <el-form-item
            v-for="(value,idx) in emp.infomap"
            :key="Math.random()"
            :label="value.name"
            width="180px"
        >
          <el-input
              placeholder="请打分"
              v-model.trim="value.content"
              clearable
          >
          </el-input>
        </el-form-item>
        <el-form-item label="编号:" prop="code">
          <el-input
              size="mini"
              style="width: 200px"
              prefix-icon="el-icon-edit"
              v-model="emp.code"
              placeholder="请输入编号"
          ></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
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

              <el-button @click="update(scope.row)" style="padding: 4px" size="mini" icon="el-icon-check" type="primary"
                         plain>调换到本组：{{keywords_name}}</el-button>

            </template>
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible_edit = false">关闭</el-button>
        </span>
    </el-dialog>
    <!--弹窗-->
    <el-dialog :title="title" :visible.sync="dialogVisible_checkbox" width="90%" center>
<!--      <div style="font-size: 17px;">-->
<!--        导入模板中必须包含身份证号和姓名，以下勾选的列将包含在导入模板中。模板中不包含的列，则导入时将保持数据库中已有信息不变。-->
<!--        <br/>-->
<!--        首次导入请注意！如果student表中已有该选手的记录，则手机号、邮箱、属于本单位三列可为空，“属于本单位”列填是或否。用户名密码可以不填写，若不填写第一次导入将默认为编号和手机号，其余必须填写。-->
<!--        <br/>模版内的列顺序为点击顺序。-->
<!--      </div><br/>-->
        <div style="font-size: 17px;">
            导入模板中必须包含姓名和身份证号，以下勾选的列将包含在导入模板中。模板中不包含的列，则导入时将保持数据库中已有信息不变。
            <br/>
        </div><br/>
      <div style="font-size: 16px;margin-left: 15%">基本信息：<br/>
      <el-checkbox label="姓名" key="姓名" v-model="dymatic_list" disabled style="width: 150px">姓名</el-checkbox>
<!--      <el-checkbox label="身份证号码" v-model="dymatic_list" disabled style="width: 150px">身份证号码</el-checkbox>-->
       <el-checkbox label="编号" v-model="dymatic_list" disabled style="width: 150px">编号</el-checkbox>
       <el-checkbox label="组名" v-model="dymatic_list"  style="width: 150px">组名</el-checkbox>
      <el-checkbox label="序号" v-model="dymatic_list"  style="width: 150px">组内序号</el-checkbox>
      <el-checkbox label="手机号" v-model="dymatic_list"  style="width: 150px">手机号</el-checkbox>
      <el-checkbox label="邮箱" v-model="dymatic_list"  style="width: 150px">邮箱</el-checkbox>
<!--      <el-checkbox label="属于本单位" v-model="dymatic_list"  style="width: 150px">属于本单位</el-checkbox>-->
<!--      <el-checkbox label="用户名" v-model="dymatic_list"  style="width: 150px">用户名</el-checkbox>-->
<!--      <el-checkbox label="密码" v-model="dymatic_list"  style="width: 150px">密码</el-checkbox>-->
      </div><br/>
      <div style="font-size: 16px;margin-left: 15%">信息项：<br/>
        <el-checkbox v-for="item in infoitem_from_back" :key="item.name" :label="item.name" v-model="infoitem" style="width: auto">
        </el-checkbox>
      </div><br/>
      <div style="font-size: 16px;margin-left: 15%" class="formView">评分项：<br/>
        <el-checkbox v-for="item in scoreitem_from_back" :key="item.name" :label="item.name" v-model="scoreitem" style="width: auto">
        </el-checkbox>
      </div>
        <div style="font-size: 16px;margin-left: 15%;margin-top: 15px">模版中的列排列顺序如下：<br/></div>
      <div style="font-size: 16px;margin-left: 15%">{{preview(dymatic_list,scoreitem,infoitem)}}</div>
      <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="exportMo">下载</el-button>
          <el-button @click="dialogVisible_checkbox = false">关闭</el-button>
        </span>
    </el-dialog>
    <el-dialog :title="title" ref="dia" :visible.sync="dialogVisible_method" width="55%" center @close="handleClose">
      <el-tabs type="border-card">
        <el-tab-pane label="手动添加">
          <el-form class="registerContainer" ref="manualAddForm" :rules="manualAddRules" :model="manualAddForm">
            <el-form-item label="身份证号:" prop="idnumber" >
              <el-input style="width: 60%"  v-model="manualAddForm.idnumber" @blur="getInfoByIDNumber()"></el-input>
            </el-form-item>
            <el-form-item label="编号:" prop="code" >
              <el-input style="width: 60%" v-model="manualAddForm.code"></el-input>
            </el-form-item>
            <el-form-item label="姓名:" prop="name" >
              <el-input style="width: 60%" v-model="manualAddForm.name" :disabled="manualAddFormDisabled"></el-input>
            </el-form-item>
            <el-form-item label="电话:" prop="telephone">
              <el-input style="width: 60%" v-model="manualAddForm.telephone" :disabled="manualAddFormDisabled"></el-input>
            </el-form-item>
            <el-form-item label="邮箱:" prop="email">
              <el-input style="width: 60%" v-model="manualAddForm.email" :disabled="manualAddFormDisabled"></el-input>
            </el-form-item>
          </el-form>
          <el-button type="primary" @click="manualAdd">添加</el-button>
        </el-tab-pane>
        <el-tab-pane label="从本单位添加">
          <el-input
              v-model="searchText"
              placeholder="请输入学号或姓名进行搜索"
              @keyup.enter.native="search"
              @input="search"
          >
            <template #append>
              <el-button icon="el-icon-search" type="success" @click="search"></el-button>
            </template>
          </el-input>
          <el-table
              ref="multipleTable"
              :data="currentParticipants"
              tooltip-effect="dark"
              style="width: 100%"
              @selection-change="handleSelectionChange"
              :row-key="getRowKeys">
            <el-table-column
                :reserve-selection="true"
                type="selection"
                :selectable="checkSecletion"
                width="40px">
            </el-table-column>
            <el-table-column
                prop="studentNumber"
                label="学号"
                show-overflow-tooltip>
            </el-table-column>
            <el-table-column
                prop="name"
                label="姓名">
            </el-table-column>

          </el-table>
          <div class="block" style="padding-top: 10px">
            <el-pagination
                @current-change="currentChange_all"
                @size-change="sizeChange_all"
                :current-page="page"
                layout="sizes, prev, pager, next, jumper, ->, total, slot"
                :total="total">
            </el-pagination>
          </div>
          <div style="color: #4b8ffe ;float: right">
            已选择{{multipleSelection.length}}位选手
            <el-button type="primary" @click="add" style="padding-left: 10px">
              添加
            </el-button>
          </div>
        </el-tab-pane>
        <el-tab-pane label="批量导入">
          <a>
            选手第一次导入时，可先不分组。此时可以将导入表格中的“分组名称”留空，进行导入操作。待分组后，再导入一次，从而实现分组。
            选手的信息项以及评分项，也可在选手第一次导入时留空，待第二次、第三次（或之后）导入时填入那些信息。<br/><br/>
          </a>
          <span style="font-weight:600;">导入新数据</span> 第一步：
          <el-button type="primary" @click="exportCheckbox" icon="el-icon-upload" style="margin-right: 8px">
            下载模板
          </el-button>
          第二步：
          <!--          导入选手按钮-->
          <el-upload
              :show-file-list="false"
              :before-upload="beforeUpload"
              :on-success="onSuccess"
              :on-error="onError"
              :disabled="importDataDisabled"
              style="display: inline-flex;"
              action="#"
              :http-request="handleChange">
            <el-button :disabled="importDataDisabled" type="primary" :icon="importDataBtnIcon">
              {{importDataBtnText}}
            </el-button>
          </el-upload>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import {Message} from "element-ui";
import axios from 'axios'
import {validateInputIdCard} from "@/utils/check";
import PinYinMatch from "pinyin-match";
import AddActStep from "@/components/AddActStep.vue";
export default {
  name: "SalGroup",
 components: {AddActStep},
  data() {
    return{
      mode:'',
      activeName:'participant',
      searchText: '',
      filterParticipantsByState:'',
      filterParticipantsByStateList:['全部显示','未分组','已分组'],//筛选选手下拉框
      sortBy:'',
      sortByList:[],//排序依据 包括评分项和信息项
      groupNumsInput:[],//用于存放选择分为几组后的div和input框
      radio:'1',
      filterNoGroupPar:[],//对话框中的展示table数据
      groupSubOfSelectedInfos:[],//对子信息项再进行选择
      selectedSubGroupInfo:[],//选择的子信息项
      selectedGroupNums:0,//选择的分组数量
      groupNums:null,//分数个数列表
      selectedGroupInfo:'',//被选择的信息项
      groupInfoNums: {},//所有信息项 选项
      dialogPartipicantGroups:false,//选手分组的对话框
      title: '',
      labelPosition: "left",
      importDataBtnText: '导入选手',
      importDataBtnIcon: 'el-icon-plus',
      importDataDisabled: false,
      showAdvanceSearchView: false,
      manualAddFormDisabled: false,
      show:false,
      percentage: '',
      keywords_name: "",
      datalist:'',
      allDeps: [],
      emps: [],
      unsureinfo: [],
      dymatic_list:["姓名","编号"],
      scoreitem:[],
      infoitem:[],
      scoreitem_from_back:[],
      infoitem_from_back:[],
      participants:[],
      currentParticipants:[],
      multipleSelection: [],
      dialogVisible_edit: false,
      dialogVisible_checkbox: false,
      dialogVisible_method:false,
      loading: false,
      dialogVisible: false,
      total: 0,
      page: 1,
      size: 10,
      total_all: 0,
      page_all: 1,
      size_all: 10, //用于添加选手里的分页
      keywords: '',
      keyword: '',
      activityID: '',
      groupID: 0,
      positions: [],
      emp: {
        id:null,
        institutionID:null,
        name: null/*,*/
        /*startDate: '2022/02/02',
        scoreItemCount: '0',
        score: '100',
        groupCount: '0',
        expertCount: '0',
        participantCount: '0',
        comment: "javaboy",*/
      },
      manualAddForm:{
        code:'',
        name: '',
        telephone: '',
        idnumber: '',
        email:'',
      },
      defaultProps: {
        children: 'children',
        label: 'name',
      },
      rules: {
        name: [{required: true, message: '请输入活动名', trigger: 'blur'}],
        startDate: [{required: true, message: '请输入活动时间', trigger: 'blur'}],
        scoreItemCount: [{required: true, type: 'number', message: '请输入正确数据', trigger: 'blur', transform: (value) => Number(value)}],
        comment: [{required: true, message: '请输入备注', trigger: 'blur'}],
      },
      manualAddRules:{
        idnumber:[
          { validator: validateInputIdCard, trigger: "blur" }]
      }
    }
  },
  computed: {
    user() {
      return JSON.parse(localStorage.getItem('user'));//object信息
    }
  },
  watch:{
    selectedGroupNums:{//监听用户选择了分为几组
      handler(val){
        //信息项被选择
        if(this.selectedGroupInfo != ''){
              this.groupNumsInput = []
              for(var i = 0;i < val;i ++){
                this.groupNumsInput.push({
                  idx:i,
                  value:'',
                  disabled:true//默认是均分
                })
              }
              if(this.radio == '2'){//如果是指定人数
                for(var item of this.groupNumsInput){
                  item.disabled = false
                }
              }
          this.calculationGroupInputValue()
        }
      }
    },
    radio:{
      handler(val){
        if(this.radio == '1'){//均分
          for(var i = 0;i < this.selectedGroupNums;i ++){
            this.groupNumsInput[i].disabled = true
          }
        }else if(this.radio == '2'){//指定每组人数
          for(var i = 0;i < this.selectedGroupNums;i ++){
            this.groupNumsInput[i].disabled = false
          }
        }
        this.calculationGroupInputValue()
      }
    },
    selectedGroupInfo:{//监听第一个下拉框的变化 信息项
      handler(val){
        //信息项和信息项的子选项都被选择了或者没有子信息项
        if(val != '') {
            //该信息项下的所有子信息项
            if (JSON.stringify(this.groupInfoNums) != '{}' && JSON.stringify(this.groupInfoNums[val]) != '{}') {
              this.groupSubOfSelectedInfos = Object.keys(this.groupInfoNums[val])
              this.filterNoGroupParticipants()
            }
        }
      }
    },
    selectedSubGroupInfo:{//第二个下拉框的变化 信息项的子信息项
      handler(val){
        if(this.selectedGroupInfo != ''){//信息项和信息项的子选项都被选择了
          this.filterNoGroupParticipants()
        }
      }
    }
  },
  created() {
  },
  mounted() {
    this.activityID = this.$route.query.activityID;
    this.mode = this.$route.query.mode
    this.groupID = this.$route.query.groupID
    this.keywords = this.$route.query.keywords;
    this.keywords_name = this.$route.query.keyword_name;
    this.ACNAME=this.$route.query.keywords_name;
    this.initEmps();
  },
  methods: {
    preview(dymatic_list,infoitem,scoreitem){
       // 拼接3个list，然后转换为不带有引号的字符串
        var list = dymatic_list.concat(scoreitem).concat(infoitem);
        var str = list.join()
        return str
    },
    filterParticipantsByStateMed(val){
      if(val == '' || val == '全部显示'){
        this.initEmps()
      }else {
        this.loading = true;
        let url = '/participants/basic/filterByState?page=' + this.page + '&size=' + this.size+ '&groupState='+ val + '&activitiesID='+this.keywords;
        this.getRequest(url).then(resp => {
          this.loading = false;
          if (resp) {
            this.emps = resp.data;
            this.total = resp.total;
            this.page = 1
          }
        });
      }
    },
    search() {
      if (this.searchText === ''){
        this.participants = this.participants_raw
      }else if (/^\d+$/.test(this.searchText)){ // 纯数字，按工号搜索
        this.participants = this.participants_raw.filter(item => item.studentNumber != null &&  item.studentNumber.includes(this.searchText))
      }else if (/^[a-zA-Z]*$/.test(this.searchText)){ //纯英文，考虑首字母
        //const PinyinMatch = require('pinyin-match');
        this.participants = this.participants_raw.filter(item => PinYinMatch.match(item.name,this.searchText))
      } else { // 非纯数字，按姓名搜索
        this.participants = this.participants_raw.filter(item => item.name.includes(this.searchText))
      }
      this.total_all = this.participants.length
      this.getCurrentParticipants()
      this.page_all = 1
    },
    deleteGroupsOfParticipant(){//删除分组 这个活动下的所有group
        this.$confirm('是否确认清空分组信息？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(()=>{
          var url = '/participants/basic/deleteGroups?activityID=' + parseInt(this.keywords)
          this.getRequest(url).then((resp)=>{
            if(resp){
              this.$message.success(resp.msg)
              this.filterParticipantsByState = ''
              this.initEmps();
            }
          })
        })
        .catch(()=>{})
    },
    calculationGroupInputValue(){//计算input框的值
      //如果没有选择分组个数或没有待分组的选手直接返回
      if(this.filterNoGroupPar.length == 0 || this.selectedGroupNums == 0){
        return
      }
      if(this.selectedGroupNums > this.filterNoGroupPar.length){
        this.$message.warning('分组数不能超过未分组人数！')
        return
      }
      var participantNums = this.filterNoGroupPar.length//选手人数
      var groupNums = this.selectedGroupNums//组数
      if(this.selectedGroupNums != null){//均分
        var baseNums = Math.floor(participantNums / groupNums);
        for(var i = 0;i<groupNums;i++){
          this.groupNumsInput[i].value = baseNums;
        }
        for(var i = 0;i<(participantNums - baseNums * groupNums);i ++){
          this.groupNumsInput[i].value ++;
        }
      // }else if(this.radio == '2'){//指定人数 groupNumsInput保存每组的指定人数
      //   for(var i = 0;i<groupNums;i++){
      //     this.groupNumsInput[i].value = '';
      //   }
      }
    },
    closeDialogGroupOfParticipant(){//选手分组对话框关闭,清空遗留数据
      if(this.dialogPartipicantGroups){
        this.dialogPartipicantGroups = false
      }
      this.selectedGroupInfo = ''
      this.selectedSubGroupInfo = []
      this.selectedGroupNums = 0
      this.filterNoGroupPar = []
      this.groupSubOfSelectedInfos = []
    },
    filterNoGroupParticipants(){//信息项已经选择
      //通过2个id找infos的选手id， 通过活动id，选手id找没有分组的选手，返回信息
      //有子信息项但是没有选择或者没选信息项就返回
      if(this.selectedGroupInfo == '' ){
        return
      }
      //有子信息项但是没选择
      // if((this.selectedSubGroupInfo.length == 0 &&
      //     this.selectedGroupInfo != '' &&
      //     JSON.stringify(this.groupInfoNums[this.selectedGroupInfo]) != '{}')){
      //
      // }
      var data = {
        'activityID':[this.keywords.toString()],
      }
      // if(this.selectedSubGroupInfo.length > 0){//判断有没有选择信息项
        data['infoItemID'] = [this.groupInfoNums[this.selectedGroupInfo].infoItemID]
        data['infoContent']= this.selectedSubGroupInfo
      // }
      var url = '/info/basic/getPartipicantIDByInfos'
      this.postRequest(url,data).then((res)=>{
        if(res){
          // console.log('没有分组的选手:')
          // console.log(res.obj)
          this.filterNoGroupPar = res.obj//存放没有分好组的选手，但是不显示在页面上
          if(this.filterNoGroupPar.length == 0){
            this.$message.warning('该信息项下已分组!')
          }
          this.calculationGroupInputValue()
        }
      })

    },
    groupsForParticipant(){//选手分组,点击按钮对话框弹出 //获得该活动下的所有Infoitem，其中包括content
      if(this.emps == null || this.emps.length == 0){
        this.$message.warning('请先导入选手！')
        return
      }
      this.groupSubOfSelectedInfos = []
      this.groupInfoNums = {}
      this.sortByList = []
      var url = '/infoItem/basic/getAll/' + this.keywords
      this.getRequest(url).then((resp)=>{
        if(resp.code == 200){
          //存放infoItem
          // console.log(resp)
          var infoItems = resp.extend.infoItems
          if(infoItems.length === 0){   // bug
            this.$message.warning('该活动下没有未分组的选手！')
            return
          }
          //数据处理应该由后端处理的。。。
          for(var i = 0;i < infoItems.length;i ++){
            if(!(infoItems[i].name in this.groupInfoNums)){
              this.groupInfoNums[infoItems[i].name]={'infoItemID':infoItems[i].id}//将每一个信息项改为对象形式,再加上每个信息项的id
            }
            //如果每个信息项包含多个子信息项如报考专业包括电子xxx、xx开发等，将每个子信息项改为数组
            if(!(infoItems[i].content in this.groupInfoNums[infoItems[i].name])){
              this.groupInfoNums[infoItems[i].name][infoItems[i].content] = []
            }
            this.groupInfoNums[infoItems[i].name][infoItems[i].content].push(infoItems[i])
          }
          //将infoItem作为排序依据
          for(var i of Object.keys(this.groupInfoNums)){
            this.sortByList.push({
              type:'信息项',
              name:i,
              id:this.groupInfoNums[i].infoItemID
            })
          }
          if(!this.groupNums){
            this.groupNums = Array.from(Array(10).keys(),n=>n+1)
          }
          this.dialogPartipicantGroups = true
        }
      })
    },
    creatGroup(){//创建分组//传递activityID和选手id，分为几组和每组人数
      var sum = 0
      if(this.radio == '2'){
        this.groupNumsInput.map(item=>{
          sum += parseInt(item.value)
        })
        if(sum != this.filterNoGroupPar.length){
          this.$message.error('待创建分组人数为' + this.filterNoGroupPar.length + '，分组总人数为' + sum + '，人数不相等！')
          return
        }
      }
      var url = '/groups/basic/createGroups'
      //每组人数
      var arr = this.groupNumsInput.map(item=>{
        return parseInt(item.value)
      })
      var data = {
        'activityID':parseInt(this.keywords),
        'arr':arr,
        'exchangeNums':Math.ceil(this.selectedGroupNums / 2),
        'groupsNums':this.selectedGroupNums
      }
      if(this.sortBy != ''){
        var obj = this.sortByList.find(item => item.name == this.sortBy)
        data['sortByItemType'] = obj.type
        data['sortByItemID'] = obj.id
      }else {
        return;
      }
      data['infoItemID'] = this.groupInfoNums[this.selectedGroupInfo].infoItemID
      data['infoContent']= this.selectedSubGroupInfo
      this.postRequest(url,data).then((resp)=>{
        if(resp){
          if(resp == "分组成功" ){
            this.$message.success(resp)
            this.initEmps()
          }else {
            this.$message.error(resp)
          }
          this.dialogPartipicantGroups = false
          this.filterParticipantsByState = ''
          this.closeDialogGroupOfParticipant()
        }
      })
    },
    /** 查询角色列表 */
    onError(err, file, fileList) {
      this.importDataBtnText = '导入选手';
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataDisabled = false;
    },
    onSuccess(res) {
      // console.log(res);
      this.$message(res.msg);
      this.importDataBtnText = '导入选手';
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataDisabled = false;
      this.initEmps();
    },
    beforeUpload() {
      let url = "/participants/basic/check?groupid=0";
      //let url = "/participants/basic/import?groupid=0"+"&activityid="+this.keywords+"&insititutionID="+this.user.institutionID;
      return  url;
      this.importDataBtnText = '正在导入';
      this.importDataBtnIcon = 'el-icon-loading';
      this.importDataDisabled = true;
    },
    handleChange(file) {
      this.show = true;
      var that = this
      let fd = new FormData();
      let fileName = file.file.name + new Date().getTime();
      fd.append("file", file.file);
      fd.append("key", fileName);
      let url = "/participants/basic/check?groupid=0";
      this.postRequest(url, fd, {
            headers: {
              "Content-Type": "multipart/form-data",
              'token':this.user.token
            },
          })
          .then((res1) => {
            if(res1.length===0) // 数据完整，没有空数据
            {
              url = "/participants/basic/import?groupid=0"+"&activityid="+this.keywords+"&insititutionID="+this.user.institutionID;
              axios.post(url, fd, {
                headers: {
                  "Content-Type": "multipart/form-data",
                  'token':that.user.token
                },
              }).then((res) => {
                this.$message(res.msg);
              })
            }
            else{
              let newD=[],h=this.$createElement;
              // newD.push(h('p',null,'确认导入数据？'));
              // newD.push(h('p',null,'导入数据中'));
              var count = 0
              for(const i in res1)
              {
                count++
                newD.push(h('p',null,res1[i]))
                if (count === 15) // 最多显示15行
                  break
              }
              newD.push(h('p',null,'是否确认继续?'));
              this.$confirm(h('div',null,newD), '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                that.loading = true
                url = "/participants/basic/import?groupid=0"+"&activityid="+this.keywords+"&insititutionID="+this.user.institutionID;
                axios.post(url, fd, {
                  headers: {
                    "Content-Type": "multipart/form-data",
                    'token':that.user.token
                  },
                }).then((res) => {
                  that.loading = false
                  this.initEmps();
                  this.$message(res.msg);
                })
              })
            }
          })
          .catch((err) => {
            // console.log(err);
          });

    },
    change2Exp(){
      const _this = this;
      _this.$router.push({
        path: "/ActivitM/sobcfg",
        query: {
          activityID: this.keywords,
          keywords: this.keywords,
          keyword_name: this.keywords_name,
          keywords_name:this.keywords_name,
          groupID: this.groupID,
          backID: this.activityID,
          mode:this.mode,
          haveSub:this.$route.query.haveSub,
          addActive:this.$route.query.addActive,
        }
      })
    },
    handleClose(){
      this.$refs.manualAddForm.resetFields();
      this.manualAddFormDisabled=false;
      this.searchText = ''
      this.multipleSelection = []
      this.participants = this.participants_raw
      this.total_all = this.participants.length
      this.page_all = 1
      this.$refs.multipleTable.clearSelection()
    },
    exportData() {
      this.loading=true;
      Message.success("正在导出");
      let url = "/participants/basic/export?activityID="+this.keywords;
      window.open(url, '_parent');
      this.loading=false;
    },
    exportCheckbox() {
      let url = "/participants/basic/getItem?activityid="+this.keywords;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
            // console.log("resp")
            // console.log(resp)
          this.scoreitem_from_back=resp.extend.ScoreItem;
          this.infoitem_from_back=resp.extend.InfoItem;
        }
      });
      this.title = "选择导入模板的数据列";
      this.dialogVisible_checkbox=true;
    },
    exportMo() {
      this.dialogVisible_checkbox=false;
      Message.success("正在下载模板");
      let url = "/participants/basic/exportMoPar_Group?activityid="+this.keywords+"&dymatic_list="+this.dymatic_list+"&scoreitem="+this.scoreitem+"&infoitem="+this.infoitem;
      window.open(url, '_parent');
    },
    showEditEmpView(data) {
      this.title = "编辑选手信息";
      this.emp = data;
      this.dialogVisible = true;
    },
    Delete(data) {
      data.institutionid= this.user.institutionID;
      this.$confirm('此操作将永久删除【' + data.name + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (data.groupID === null)
            data.groupID = -1
        this.postRequest("/participants/basic/delete?groupID="+data.groupID,data).then(resp => {
          if (resp) {
            if (resp.status === 200) {
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
              this.initEmps();
            } else {
              this.$message({
                type: 'error',
                message: '删除失败!'
              });
            }
            this.dialogVisible = false;
            this.initEmps();
          }
        })
      })
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      if(this.filterParticipantsByState == ''){
        this.initEmps();
      }else {
        this.filterParticipantsByStateMed(this.filterParticipantsByState)
      }
    },
    currentChange(currentPage) {
      this.page = currentPage;
      if(this.filterParticipantsByState == ''){
        this.initEmps('advanced');
      }else {
        this.filterParticipantsByStateMed(this.filterParticipantsByState)
      }
    },
    sizeChange_all(currentSize) {
      this.size_all = currentSize;
      this.getCurrentParticipants();
    },
    currentChange_all(currentPage) {
      this.page_all = currentPage;
      this.getCurrentParticipants('advanced');
    },
    initEmps() {
      this.loading = true;
      let url = '/participants/basic/?page=' + this.page + '&size=' + this.size+ '&groupID=0'+ '&activitiesID='+this.keywords;
      //console.log(url);
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          // console.log("aha",resp);
          this.emps = resp.data;
          this.total = resp.total;
          //console.log("total",this.total);
        }
      });
      this.initParticipants();
    },
    back(){
      const _this = this;
      var url = ""
      if (this.mode === 'admin'){
        url = "/ActivitM/search"
      }else if (this.mode === "secretary"){
        url = "/secretary/ActManage"
      }else if (this.mode === "adminSub"){
        url = "/ActivitM/SubActManage"
      }else if (this.mode === "secretarySub"){
        url = "/secretary/SubActManage"
      }
      _this.$router.push({
        path: url,
        query: {
          id: _this.$route.query.backID,
          mode: _this.mode,
        }
      });
    },
    refreshact() {
      this.initEmps();
    },
    save() {
      this.emp.institutionid= this.user.institutionID;
      this.dialogVisible = false;
      const _this = this;
      // console.log("-----------",_this.hrs);
      //this.$router.push({name:'/scoreItem/basic/update',params:{scoreItem:_this.hrs,total:_this.total}})
      this.postRequest("/participants/basic/update", _this.emp).then((resp) => {
      });
    },
    initParticipants(){
      this.loading = true;
      let url = '/participants/basic/getByInstitutionID/?institutionID=' + this.user.institutionID;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.participants = resp.obj;
          this.participants_raw = resp.obj;
          this.total_all = this.participants.length;
        }
      });
    },
    showMethod(){
      this.title = "添加选手";
      this.dialogVisible_method=true;
      this.getCurrentParticipants();
    },
    getInfoByIDNumber(){
      if (this.manualAddFormDisabled === true){
        this.manualAddForm = {idnumber: this.manualAddForm.idnumber}
      }
      this.manualAddFormDisabled = false
      this.getRequest("/participants/basic/getByIDNumber?IDNumber="+this.manualAddForm.idnumber).then(resp => {
        if (resp && resp.obj != null){
          this.manualAddForm = {
            code:resp.obj.code,
            name: resp.obj.name,
            telephone: resp.obj.telephone,
            idnumber: resp.obj.idnumber,
            email:resp.obj.email,
          }
          this.manualAddFormDisabled = true
        }
      })
    },
    manualAdd(){
      {
        this.manualAddForm.institutionid = this.user.institutionID;
        this.manualAddForm.activityID = this.keywords
        this.manualAddForm.groupID = this.groupID
        this.manualAddForm.IDNumber = this.manualAddForm.idnumber
        this.$refs['manualAddForm'].validate((valid) => {
          if (valid) {
            this.postRequest1("/participants/basic/manualAdd",this.manualAddForm).then(resp => {
              if (resp && resp.status === 200) {
                this.dialogVisible_method = false
                this.initEmps(this.emps.length);
              }
            });
          } else {
            return false
          }
        })
      }

    },
    handleSelectionChange(val){
      for(let i=0;i<val.length;i++){
        for (let j=0;j<this.emps.length;j++){
          if (val[i].idnumber===this.emps[j].idnumber){
            val.splice(i,1);
            i--;
            break;
          }
        }
      }
      this.multipleSelection=val;
    },
    getRowKeys(row) {
      return row.name;
    },
    checkSecletion(row,index){
      for (let i = 0; i < this.emps.length; i++){
        if (row.idnumber === this.emps[i].idnumber)
          return false;
      }
      return true;
    },
    getCurrentParticipants(){
      let begin = (this.page_all - 1) * this.size_all;
      let end = this.page_all * this.size_all;
      this.currentParticipants = this.participants.slice(begin, end);
      this.$nextTick(() => {
        this.currentParticipants.forEach(item => {
          for (let i = 0; i < this.emps.length; i++){
            if (item.idnumber === this.emps[i].idnumber)
              this.$refs.multipleTable.toggleRowSelection(item, true)
          }
        })
      })
    },
    add(){
      if (this.multipleSelection.length === 0) {
        this.$message({
          type: 'warning',
          message: '请选择选手!'
        });
        return;
      }
      this.dialogVisible_method = false;
      const _this = this;
      this.groupID=0;
      console.log(this.groupID);
      this.postRequest("/participants/basic/addPars?activityID="+this.keywords + "&groupID=" + this.groupID,_this.multipleSelection).then((resp) => {
        if (resp) {
          this.initEmps();
          this.$message({
            type: 'success',
            message: '添加成功!'
          });
        }
      });
    }
  }
}
</script>

<style scoped>
.formView >>>.el-checkbox__label {
  display: inline-grid;
  white-space: pre-line;
  word-wrap: break-word;
  overflow: hidden;
  line-height: 20px;
}
.inputOfGroupsBox{
  /*word-wrap: break-word;*/
  /*word-break: break-all;*/
  margin: auto;
  width: 520px;
  position: relative;
  text-align: center;
  margin-top: 17px;
}
/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all .8s ease;
}

.slide-fade-leave-active {
  transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */
{
  transform: translateX(10px);
  opacity: 0;
}
</style>

