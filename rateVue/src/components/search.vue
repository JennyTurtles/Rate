<template>
  <div>
    <el-breadcrumb separator-class="el-icon-arrow-right" style="float: left;margin-top: 10px;font-size: 15px" v-show="isRoot">
      <el-breadcrumb-item style="margin-left: 5px"> {{p1}}</el-breadcrumb-item>
      <el-breadcrumb-item>{{category}}（{{indicatorTypeZH}}）：{{score}}分</el-breadcrumb-item>
    </el-breadcrumb>
    <!--    选择年份-->
    <el-select style="float: left;margin-left: 20px;width: 80px" v-model="year" v-show="isRoot" @change="changeYear">
      <el-option
          v-for="item in years"
          :key="item"
          :label="item"
          :value="item">
      </el-option>
    </el-select>
    <div style="float: left;margin-top: 4px;margin-left: 3px" v-show="isRoot">年</div>
    <div style="text-align: center;margin-top: 100px;font-size:30px" v-show="!isRoot">请从左边选择一个分类</div>
    <!--添加-->
    <div style="padding-bottom: 40px;margin-top: 5px ;margin-right: 10px" v-show="isRoot">
      <el-button type="primary" style="float: right;margin-left: 10px" @click="()=>{
        publicationInf.year = year
        if (indicatorType === 'publication')
          this.dialogVisibleAppendPublication = true;
        else if (indicatorType === 'award')
          this.dialogVisibleAppendAward = true;
        else if (indicatorType === 'program')
          this.dialogVisibleAppendProgram = true;
         else if (indicatorType === 'decision')
          this.dialogVisibleAppendDecision = true;
      }" icon="el-icon-circle-plus">添加</el-button>
      <el-button type="primary" style="float: right;margin-left: 10px" @click="uploadVisible=true;importSelectYear = year;uploadResultError = false" icon="el-icon-s-order">批量导入</el-button>
      <el-button @click="searchUnAvailable = false;dialogVisibleSearch = true" type="primary" style="float: right" icon="el-icon-search" >搜索</el-button>

    </div>
    <!--    点击搜索按钮-->
    <el-dialog
        :visible.sync="dialogVisibleSearch"
        @close="closeSearch"
        width="90%">
      <span slot="title" style="float:left;font-size: 20px" >请输入需要搜索的名称</span>
      <div style="margin-bottom: 10px">
        <span>请选择搜索的类别：</span>
        <el-select
            style="margin-right: 20px;width: 120px;"
            v-model="searchSelectType"
        >
          <el-option
              v-for="item in selectTypes"
              :key="item"
              :label="item"
              :value="item">
          </el-option>
        </el-select>
      </div>

      <div class="select_div_input">
        <input
            autocomplete="off"
            style="margin-left:5px;width:30%;line-height:28px;
                              border:1px solid lightgrey;padding:0 10px 1px 15px;
                              border-radius:4px;color:gray"
            placeholder="请输入期刊名称"
            v-model="publicationName"
            @focus="ispubShow = true"
            @blur="ispubShow=ispubFlag"
            id="input_publicationName"/>
        <div class="select_div"
             v-show="ispubShow && publicationName ? true:false"
             :style="'height:${menuHeight}'"
             @mouseover="ispubFlag = true"
             @mouseleave="ispubFlag = false">
          <div
              class="select_div_div"
              v-for="(val,idx) in select_pubName"
              :key="idx"
              :value="val.value"
              @click="filter_pub(val.value)"
          >
            {{ val.value }}
          </div>
        </div>
      </div>
      <!--      <div style="margin-top: 10px" v-show="pathVisible">分值：{{searchPathInf.score}}分</div>-->
      <!--      <div style="margin-top: 10px" v-show="pathVisible">类型：{{searchPathInf.type}}</div>-->
      <!--      <div style="margin-top: 10px" v-show="pathVisible">分类：{{searchPathInf.name}}</div>-->
      <!--      searchInf2-->
      <el-table v-if="searchPathInf.type === '论文'"
                :data="listSearchPublicationsByName"
                border
                style="width: 100%">
        <el-table-column
            fixed
            prop="indicatorName"
            label="指标点名称">
        </el-table-column>
        <el-table-column
            fixed
            width="50px"
            prop="score"
            align="center"
            label="分值">
        </el-table-column>
        <el-table-column
            fixed
            prop="name"
            label="刊物全称">
        </el-table-column>
        <el-table-column
            prop="abbr"
            label="刊物简称">
        </el-table-column>
        <el-table-column
            prop="publisher"
            label="出版社">
        </el-table-column>
        <el-table-column
            prop="year"
            label="录入年份"
            align="center"
            width="70px"
        >
        </el-table-column>
        <el-table-column
            prop="level"
            label="收录级别">
        </el-table-column>
        <el-table-column
            fixed="right"
            label="操作"
            width="150">
          <template slot-scope="scope">
            <el-button @click="rowData=JSON.parse(JSON.stringify(scope.row));dialogVisibleUpdatePublication=true" size="mini">编辑</el-button>
            <el-button @click="remove(scope.row.id,indicatorType)" type="danger" size="mini">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button type="danger" v-show="pathVisible" @click="searchDelete(searchInf2.id,searchPathInf.type)" >删除</el-button>
        <el-button type="primary" v-show="pathVisible" @click="dialogVisibleSearch = false;searchUpdate(searchPathInf.type)" >修改</el-button>
        <el-button @click="closeSearch()">关 闭</el-button>
      </span>
    </el-dialog>

    <!--批量导入-->
    <el-dialog
        :visible.sync="uploadVisible"
        @close=""
        width="80%">
      <span slot="title" style="float:left;font-size: 25px" >请上传需要导入的文件</span>
      <span>请选择导入的年份：</span>
      <el-select
          style="margin-right: 20px;width: 120px;"
          v-model="importSelectYear"
          v-show="isRoot"
      >
        <el-option
            v-for="item in years"
            :key="item"
            :label="item"
            :value="item">
        </el-option>
      </el-select>
      <span>请选择导入的类别：</span>
      <el-select
          style="margin-right: 20px;width: 120px;"
          v-model="importSelectType"
          v-show="isRoot"
      >
        <el-option
            v-for="item in selectTypes"
            :key="item"
            :label="item"
            :value="item">
        </el-option>
      </el-select>
      <div>
<!--        <el-button @click="downloadExcel('PublicationSample')" type="warning">下载Excel模板</el-button>-->
        <el-button @click="btnClickExport" type="warning">下载Excel模板</el-button>

      </div>
      <!--      </template>-->
      <span style="float:right;font-size: 16px;color: red" v-show="uploadResultError">请检查期刊全称是否都不为空</span>
      <span style="float:right;font-size: 16px;color: green" v-show="uploadResultValid">数据校验通过</span>
      <el-upload
          action
          :limit="1"
          :file-list="fileList"
          :auto-upload="false"
          accept = ".xlsx, .xls"
          :on-change="handleAdd"
          :on-remove="handleClose">
        <div style="margin-top: 10px">
          <el-button size="medium" type="primary">点击上传</el-button>
          <span style="color: gray; margin-left: 10px;font-size: 13px" >请填写模版后上传</span>
        </div>
      </el-upload>

      <el-table v-show="uploadResult" :data="tableUploadData" :row-class-name="checkUploadData"
                style="width: 100%">
        <el-table-column
            prop="所属类别"
            label="所属类别">
        </el-table-column>
        <el-table-column
            prop="刊物全称"
            label="刊物全称">
        </el-table-column>
        <el-table-column
            prop="刊物简称"
            label="刊物简称">
        </el-table-column>
        <el-table-column
            prop="出版社"
            label="出版社">
        </el-table-column>
        <el-table-column
            prop="网址"
            label="网址">
        </el-table-column>
        <el-table-column
            prop="收录级别 （收录级别1;级别2;（请用分号隔开））"
            label="收录级别 （收录级别1;级别2;（请用分号隔开））">
        </el-table-column>
      </el-table>

      <span slot="footer" class="dialog-footer">
        <el-button type="primary" v-show="uploadResultValid===true" @click="uploadConfirm()" >确认</el-button>
        <el-button @click="uploadVisible=false;handleClose()">关 闭</el-button>
      </span>
    </el-dialog>

<!--    :data="tableData.slice((currentPage-1)*PageSize,currentPage*PageSize)"-->
    <!--期刊-->
    <el-table v-if="indicatorType === 'publication'"
              :data="tableData"
              border
              :header-cell-style="{'textAlign':'center'}"
              style="width: 100%">
      <el-table-column
          prop="name"
          width="200px"
          label="刊物全称">
      </el-table-column>
      <el-table-column
          prop="abbr"
          width="80px"
          label="刊物简称">
      </el-table-column>
      <el-table-column
          prop="publisher"
          label="出版社">
      </el-table-column>
      <el-table-column
          prop="year"
          width="70px"
          label="录入年份"
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="level"
          label="收录级别">
      </el-table-column>
      <el-table-column
          fixed="right"
          label="操作"
          width="150">
        <template slot-scope="scope">
          <el-button @click="rowData=JSON.parse(JSON.stringify(scope.row));dialogVisibleUpdatePublication=true" size="mini">编辑</el-button>
          <el-button @click="remove(scope.row.id,indicatorType)" type="danger" size="mini">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--新增-->
    <el-dialog
        :visible.sync="dialogVisibleAppendPublication"
        width="30%">
      <span slot="title" style="float:left;font-size: 20px" >请输入期刊的相关信息</span>
      <el-form :model="publicationInf">
        <el-form-item label="期刊全称" >
          <el-input v-model="publicationInf.name"></el-input>
        </el-form-item>
        <el-form-item label="刊物简称" >
          <el-input v-model="publicationInf.abbr"></el-input>
        </el-form-item>
        <el-form-item label="出版社" >
          <el-input v-model="publicationInf.publisher"></el-input>
        </el-form-item>
        <el-form-item label="网址" >
          <el-input v-model="publicationInf.url"></el-input>
        </el-form-item>
        <el-form-item label="收录级别" >
          <el-input v-model="publicationInf.level" placeholder="多个级别请用英文分号隔开"></el-input>
        </el-form-item>
        <el-form-item label="录入年份" >
          <el-input v-model="publicationInf.year" :placeholder=nowYear></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleAppendPublication = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisibleAppendPublication = false;appendPublication(); "
        >确 定</el-button>
      </span>
    </el-dialog>
    <!--编辑-->
    <el-dialog
        :visible.sync="dialogVisibleUpdatePublication"
        width="30%">
      <span slot="title" style="float:left;font-size: 20px" >请输入期刊的相关信息</span>
      <el-form :model="rowData">
        <el-form-item label="期刊全称" >
          <el-input v-model="rowData.name"></el-input>
        </el-form-item>
        <el-form-item label="刊物简称" >
          <el-input v-model="rowData.abbr"></el-input>
        </el-form-item>
        <el-form-item label="出版社" >
          <el-input v-model="rowData.publisher"></el-input>
        </el-form-item>
        <el-form-item label="网址" >
          <el-input v-model="rowData.url"></el-input>
        </el-form-item>
        <el-form-item label="收录级别" >
          <el-input v-model="rowData.level" ></el-input>
        </el-form-item>
        <el-form-item label="录入年份" >
          <el-input v-model="rowData.year" ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUpdatePublication = false">取 消</el-button>
        <el-button type="primary" @click="update(indicatorType); dialogVisibleUpdatePublication = false;"
        >确 定</el-button>
      </span>
    </el-dialog>

    <!--科技奖-->
    <el-table v-if="indicatorType === 'award'"
              :data="tableData.slice((currentPage-1)*PageSize,currentPage*PageSize)"
              border
              style="width: 100%">
      <el-table-column
          fixed
          prop="name"
          label="奖项名">
      </el-table-column>
      <el-table-column
          fixed="right"
          label="操作"
          width="150">
        <template slot-scope="scope">
          <el-button @click="rowData=JSON.parse(JSON.stringify(scope.row));dialogVisibleUpdateAward=true" >编辑</el-button>
          <el-button @click="remove(scope.row.id,this.indicatorType)" type="danger" >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--新增-->
    <el-dialog
        :visible.sync="dialogVisibleAppendAward"
        width="30%">
      <span slot="title" style="float:left;font-size: 20px" >请输入科技奖的相关信息</span>
      <el-form :model="awardInf">
        <el-form-item label="奖项名" >
          <el-input v-model="awardInf.name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleAppendAward = false">取 消</el-button>
        <el-button type="primary" @click="appendAward(); dialogVisibleAppendAward = false;"
        >确 定</el-button>
      </span>
    </el-dialog>
    <!--编辑-->
    <el-dialog
        :visible.sync="dialogVisibleUpdateAward"
        width="30%">
      <span slot="title" style="float:left;font-size: 20px" >请输入科技奖的相关信息</span>
      <el-form :model="rowData" >
        <el-form-item label="奖项名" >
          <el-input v-model="rowData.name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUpdateAward = false">取 消</el-button>
        <el-button type="primary" @click="update(indicatorType); dialogVisibleUpdateAward = false;"
        >确 定</el-button>
      </span>
    </el-dialog>

    <!--纵向科研项目-->
    <el-table v-if="indicatorType === 'program'"
              :data="tableData.slice((currentPage-1)*PageSize,currentPage*PageSize)"
              border
              style="width: 100%">
      <el-table-column
          fixed
          prop="name"
          label="项目名">
      </el-table-column>
      <el-table-column
          fixed="right"
          label="操作"
          width="150">
        <template slot-scope="scope">
          <el-button @click="rowData=JSON.parse(JSON.stringify(scope.row));dialogVisibleUpdateProgram=true" size="small">编辑</el-button>
          <el-button @click="remove(scope.row.id,this.indicatorType)" type="danger" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--新增-->
    <el-dialog
        :visible.sync="dialogVisibleAppendProgram"
        width="30%">
      <span slot="title" style="float:left;font-size: 20px" >请输入纵向科研项目的的相关信息</span>
      <el-form :model="programInf">
        <el-form-item label="项目名" >
          <el-input v-model="programInf.name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleAppendProgram = false">取 消</el-button>
        <el-button type="primary" @click="appendProgram(); dialogVisibleAppendProgram = false;"
        >确 定</el-button>
      </span>
    </el-dialog>
    <!--编辑-->
    <el-dialog
        :visible.sync="dialogVisibleUpdateProgram"
        width="30%">
      <span slot="title" style="float:left;font-size: 20px" >请输入纵向科研项目的相关信息</span>
      <el-form :model="rowData" >
        <el-form-item label="项目名" >
          <el-input v-model="rowData.name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUpdateProgram = false">取 消</el-button>
        <el-button type="primary" @click="update(indicatorType); dialogVisibleUpdateProgram = false;"
        >确 定</el-button>
      </span>
    </el-dialog>

    <!--决策咨询成果-->
    <el-table v-if="indicatorType === 'decision'"
              :data="tableData.slice((currentPage-1)*PageSize,currentPage*PageSize)"
              border
              style="width: 100%">
      <el-table-column
          fixed
          prop="name"
          label="决策咨询成果名">
      </el-table-column>
      <el-table-column
          fixed="right"
          label="操作"
          width="150">
        <template slot-scope="scope">
          <el-button @click="rowData=JSON.parse(JSON.stringify(scope.row));dialogVisibleUpdateDecision=true"  size="small">编辑</el-button>
          <el-button @click="remove(scope.row.id,this.indicatorType)" type="danger" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--新增-->
    <el-dialog
        :visible.sync="dialogVisibleAppendDecision"
        width="30%">
      <span slot="title" style="float:left;font-size: 20px" >请输入纵向科研项目的的相关信息</span>
      <el-form :model="decisionInf" >
        <el-form-item label="决策咨询成果名" >
          <el-input v-model="decisionInf.name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleAppendDecision = false">取 消</el-button>
        <el-button type="primary" @click="appendDecision(); dialogVisibleAppendDecision = false;"
        >确 定</el-button>
      </span>
    </el-dialog>
    <!--编辑-->
    <el-dialog
        :visible.sync="dialogVisibleUpdateDecision"
        width="30%">
      <span slot="title" style="float:left;font-size: 20px" >请输入决策咨询成果的相关信息</span>
      <el-form :model="rowData" >
        <el-form-item label="决策咨询成果名" >
          <el-input v-model="rowData.name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUpdateDecision = false">取 消</el-button>
        <el-button type="primary" @click="update(indicatorType); dialogVisibleUpdateDecision = false;"
        >确 定</el-button>
      </span>
    </el-dialog>

    <div style="margin-top: 10px;text-align:right" v-show="isRoot">
      <el-pagination @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"
                     :current-page="currentPage"
                     :page-sizes="pageSizes"
                     :page-size="PageSize" layout="total, sizes, prev, pager, next, jumper"
                     :total="totalCount"
                     background
      >
      </el-pagination>
    </div>
<!--    <el-table v-show="false"-->
<!--              id="PublicationSample"-->
<!--              :data="tablePublicationSample"-->
<!--    >-->
<!--      <el-table-column prop="name" label="刊物全称"></el-table-column>-->
<!--      <el-table-column prop="abbr" label="刊物简称"></el-table-column>-->
<!--      <el-table-column prop="publisher" label="出版社"></el-table-column>-->
<!--      <el-table-column prop="url" label="网址"></el-table-column>-->
<!--      <el-table-column prop="level" label="收录级别"></el-table-column>-->
<!--    </el-table>-->
  </div>
</template>

<script>
import axios from "axios";
import * as XLSX from 'xlsx/xlsx.mjs'
import FileSaver from 'file-saver'
import th from "element-ui/src/locale/lang/th";
export default {

  name:'search',
  props:['category','type','order','score','p1','p2'],
  data(){
    return{
      typeOfAllPaper:[],
      typeOfAllProject:[],
      typeOfAllDecision:[],
      typeOfAllTechnical:[],
      listSearchPublicationsByName:[],
      ispubFlag:false,
      ispubShow:false,
      select_pubName:[],
      timer:null,
      publicationName:'',//搜索中要搜索的期刊
      searchSelectType:'',
      importSelectType:'论文',
      selectTypes:['论文','纵向科研项目','科技奖','决策咨询成果'],
      importSelectYear:2023,
      years:[],
      year:0,
      nowYear:new Date().getFullYear(),
      tableData:[],
      uploadResult:false,
      uploadResultValid:false,
      uploadResultError:false,
      tableUploadData:[],
      fileList:[],
      ID:0,
      indicatorID:0,
      indicatorType:"",
      indicatorTypeZH:"",
      dialogVisibleAppendPublication:false,
      dialogVisibleUpdatePublication:false,
      dialogVisibleAppendAward:false,
      dialogVisibleUpdateAward:false,
      dialogVisibleAppendProgram:false,
      dialogVisibleUpdateProgram:false,
      dialogVisibleAppendDecision:false,
      dialogVisibleUpdateDecision:false,
      dialogVisibleSearch:false,
      pathVisible:false,
      uploadVisible:false,
      rowData:"",
      searchInf:{}, //存放某个期刊（或其他）的indicatorID,名称,表格内id
      searchInf2:{}, //存放某个期刊（或其他）的具体信息
      searchResult:{}, //存放所有的结果
      searchPathInf:{type:'论文'},//存放某个期刊（或其他）的目录信息(来自indicator)
      publicationInf:{
        name:"",
        abbr:"",
        publisher:"",
        url:"",
        level:"",
        year:""
      },
      awardInf:{
        name:""
      },
      programInf:{
        name:""
      },
      decisionInf:{
        name:""
      },
      isRoot:false,
      currentPage:1,
      totalCount:0,
      pageSizes:[2,15,20,25],
      PageSize:15,
      tablePaperSample: [//论文
        {
          '刊物全称': "《东华大学计算机学报》",
          '刊物简称': "《东华计算机》",
          '出版社': "东华大学出版社",
          '网址': "https://cst.dhu.edu.cn",
          '收录级别 （收录级别1;级别2;（请用分号隔开））': "收录级别1;收录级别2",
        },
      ],
      tableTechnicalSample: [//科技奖
        {
          '奖项名称': "",

        },
      ],
      tableProjectSample: [//项目
        {
          '项目名称': "",
        },
      ],
      tableDecisionSample: [//咨询
        {
          '成果名称': "你的决策咨询成果名称（必填项）",

        },
      ],
      errorRow:[],
      searchUnAvailable:false,
    }
  },
  watch:{
    publicationName:{
      handler(val){
        this.delaySelectInput(val)
      }
    }
  },
  mounted() {
    axios.get("/indicator").then( (resp) =>  { //获得所有指标点
      var data = resp.obj[1]
      var that = this;
      data.forEach((item,idx1) => {
        if(item.children){//大类 1 2 3
          // console.log(item)
          item.children.forEach((subItem,idx2) => {//大类下面的4个小类
            if(subItem.type == '论文'){//添加所有是论文类别的，所有大类
              that.typeOfAllPaper = [...that.typeOfAllPaper,...subItem.children]
            } else if(subItem.type == '纵向科研项目'){
              that.typeOfAllProject = [...that.typeOfAllProject,...subItem.children]
            }else if(subItem.type == '科技奖'){
              that.typeOfAllTechnical = [...that.typeOfAllTechnical,...subItem.children]
            }else if(subItem.type == '决策咨询成果'){
              that.typeOfAllDecision = [...that.typeOfAllDecision,...subItem.children]
            }
          })
        }
      })
      // console.log(that.typeOfAllPaper)
    })
  },
  methods: {
    filter_pub(val){//选择下拉框的某个期刊 得到选择的期刊的id score等信息
      this.select_pubName = []
      this.publicationName=val
      this.ispubFlag=false
      this.ispubShow=false
      if(!val){
        return
      }
      var url = "/publication/getInf/" + val
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp) {
          this.listSearchPublicationsByName=[]
          console.log('resp:...')
          console.log(resp)
          if(resp.obj){
            resp.obj.forEach((item)=>{
              if(this.searchPathInf.type == '论文'){
                this.listSearchPublicationsByName.push(
                    {//保存返回期刊的id name 积分
                      abbr:item.abbr,
                      id:item.id,
                      indicatorName:item.indicatorName,
                      level:item.level,
                      name:item.name,
                      publisher:item.publisher,
                      url:item.url,
                      year:item.year,
                      score:item.score
                    })
              }else if(this.searchPathInf.type == '决策咨询成果'){
                this.listSearchPublicationsByName.push(
                    {//保存返回期刊的id name 积分
                      name:item.name,
                    })
              }else if(this.searchPathInf.type == '科技奖'){
                this.listSearchPublicationsByName.push(
                    {//保存返回期刊的id name 积分
                      name:item.name,
                    })
              }else if(this.searchPathInf.type == '纵向科研项目'){
                this.listSearchPublicationsByName.push(
                    {//保存返回期刊的id name 积分
                      name:item.name,
                    })
              }
            })
            console.log(this.searchPathInf)
          }else {
            this.$message.error('无该期刊！请重新选择时间！')
          }
          clearInterval(this.timer)
        }
      });
    },
    delaySelectInput(val){//期刊输入框 每隔300ms发送请求
      console.log("change")
      if(this.timer){
        clearInterval(this.timer)
      }
      if(!val){
        this.listSearchPublicationsByName=[]
        return
      }
      var publication={}
      publication.type = this.searchSelectType
      publication.name = val
      this.timer = setInterval(()=>{
        let url = "/publication/getNames"
        this.postRequest(url,publication).then((resp) => {
          this.loading = false;
          if (resp) {
            this.select_pubName=[]
            if(resp.obj != null){
              console.log(resp)
              for(var i=0;i<resp.obj.length;i++){
                this.select_pubName.push(
                    {//保存返回期刊的name
                      value:resp.obj[i],
                    }
                )
              }
            }else{
              this.select_pubName=[]
              this.ispubShow = false
              this.$message.error(`无期刊结果`);
            }
          }
          clearInterval(this.timer)
        });
      },300)
    },
    getTableByYear(indicatorID,year,a){

      var that = this
      axios.get("/publicationByYear?indicatorID="+indicatorID+"&year="+year+"&pageNum="+that.currentPage+"&pageSize="+that.PageSize).then(function (resp){
        that.tableData = resp.obj[0]
        that.totalCount = resp.obj[1]
      })
    },
    changeYear(){
      this.getTableByYear(this.indicatorID,this.year)
    },
    remove(id,indicatorType){
      var that = this
      this.$confirm('确定要删除该条记录吗？','提示',{type:"warning"}).then(function (){
        const index = that.tableData.findIndex((d) => d.id === id);
        that.tableData.splice(index,1)
        axios.delete("/"+indicatorType+"/" + id).then(function (resp){
          if (resp.status != 200)
            that.$message({
                  type: 'error',
                  message: '删除失败!'
                }
            )
          else
            that.$message({
              type: 'success',
              message: '删除成功!'
            })
          that.totalCount--;
          if (that.currentPage > (that.totalCount - 1) / that.PageSize + 1)
            that.currentPage--;
        })
      })
      that.closeSearch()
    },
    update(indicatorType){
      var that = this
      if (this.rowData.name === '')
      {
        alert('名称不能为空')
        return
      }
      axios.put("/"+indicatorType,this.rowData).then(function (resp){
        if (that.indicatorID === that.rowData.indicatorID) {
          that.getTableByYear(that.indicatorID,that.year)
        }
        if (resp.status != 200)
          that.$message({
            type: 'error',
            message: '修改失败!'
          });
        else
          that.$message({
            type: 'success',
            message: '修改成功!'
          });
      })
    },
    appendPublication(){
      if (this.publicationInf.name === '') {
        alert('期刊名称不能为空')
        this.dialogVisibleAppendPublication = true
        return
      }
      var postData = {
        name:this.publicationInf.name,
        abbr:this.publicationInf.abbr,
        publisher:this.publicationInf.publisher,
        url:this.publicationInf.url,
        level:this.publicationInf.level,
        indicatorID:this.indicatorID,
        year:this.publicationInf.year
      }
      var that = this
      axios.post("/publication",postData).then(function (resp){
        that.getTableByYear(that.indicatorID,that.year)
        if (resp.status != 200)
          that.$message({
            type: 'error',
            message: '添加失败!'
          });
        else {
          that.$message({
            type: 'success',
            message: '添加成功!',
          });
          that.totalCount++
        }
      })
      this.publicationInf = {}
    },
    appendPublicationAsync(){
      var token = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : ''
      var that = this
      var indicatorNames=[]

      for(var i = 0;i<this.tableUploadData.length;i++){
        indicatorNames.push(this.tableUploadData[i]['所属类别'])
      }
      var setIndicatorNames = new Set(indicatorNames)//去重
      indicatorNames = []
      for(var val of setIndicatorNames){//所有sheet的indicator名
        indicatorNames.push(val)
      }
      var promise = new Promise((resolve,reject) => {
        axios.post(
            "/publication/dels" ,
            {
              "year":this.importSelectYear,
              "indicatorNames":indicatorNames,
              headers:{
                token:token
              }
            }).then((resp) => {
          if (resp) {
            resolve(resp)
          }
        })
      })
      var publicationInfList = []
      for (let i = 0;i < this.tableUploadData.length;i++) {
        if (typeof this.tableUploadData[i]['刊物全称'] === 'undefined')
          return
        var publicationInf = {
          name: this.tableUploadData[i]['刊物全称'],
          abbr: this.tableUploadData[i]['刊物简称'],
          publisher: this.tableUploadData[i]['出版社'],
          url: this.tableUploadData[i]['网址'],
          level: this.tableUploadData[i]['收录级别'],
          year:this.importSelectYear,
          indicatorName:this.tableUploadData[i]['所属类别'],
        }
        publicationInfList.push(publicationInf)
      }
      promise.then(resp => {
        that.postRequest("/publications",publicationInfList).then( (res)=>{
          // that.getTableByYear(that.indicatorID,that.year)
        },()=>{
          that.$message({
            type: 'error',
            message: '添加失败!'
          });
        })
      })
    },
    appendAward(){
      var postData = {
        name:this.awardInf.name,
        indicatorID:this.indicatorID
      }
      var that = this
      axios.post("/award",postData).then(function (resp){
        axios.get("/award/" + that.indicatorID).then(function (resp)  {
          that.tableData = resp.obj[1]
        })
        if (resp.status != 200)
          that.$message({
            type: 'error',
            message: '添加失败!'
          });
        else
          that.$message({
            type: 'success',
            message: '添加成功!'
          });
      })
      this.awardInf = {}
    },
    appendProgram(){
      var postData = {
        name:this.programInf.name,
        indicatorID:this.indicatorID
      }
      var that = this
      axios.post("/program",postData).then(function (resp){
        axios.get("/program/" + that.indicatorID).then(function (resp)  {
          that.tableData = resp.obj[1]
        })
        if (resp.status != 200)
          that.$message({
            type: 'error',
            message: '添加失败!'
          });
        else
          that.$message({
            type: 'success',
            message: '添加成功!'
          });
      })
      this.programInf = {}
    },
    appendDecision(){
      var postData = {
        name:this.decisionInf.name,
        indicatorID:this.indicatorID
      }
      var that = this
      axios.post("/decision",postData).then(function (resp){
        axios.get("/decision/" + that.indicatorID).then(function (resp)  {
          that.tableData = resp.obj[1]
        })
        if (resp.status != 200)
          that.$message({
            type: 'error',
            message: '添加失败!'
          });
        else
          that.$message({
            type: 'success',
            message: '添加成功!'
          });
      })
      this.decisionInf = {}
    },

    getData(indicatorID,type){
      //初始化
      this.isRoot = true;
      this.indicatorTypeZH = type
      var that = this
      if (type === "论文")
        this.indicatorType = "publication"
      else if (type === "科技奖")
        this.indicatorType = "award"
      else if (type === "纵向科研项目")
        this.indicatorType = "program"
      else if (type === "决策咨询成果")
        this.indicatorType = "decision"
      this.indicatorID = indicatorID
      this.years = []
      this.year = this.nowYear
      for (var i = 0; i < 5;i++)
        this.years.push(this.nowYear-i)
      that.getTableByYear(indicatorID,that.year)

    },
    closeSearch(){
      this.dialogVisibleSearch = false
      this.pathVisible = false
      this.searchInf=''
      // this.searchPathInf = ''
    },
    searchUpdate(indicatorType){
      this.rowData = this.searchInf2
      var type = ""
      if (indicatorType === '论文')
        type = 'publication'
      else if (indicatorType === '纵向科研项目')
        type = 'program'
      else if (indicatorType === '科技奖')
        type = 'award'
      else if (indicatorType === '决策咨询成果')
        type = 'decision'
      this.update(type)
    },
    searchDelete(id,indicatorType){
      var type = ""
      if (indicatorType === '论文')
        type = 'publication'
      else if (indicatorType === '纵向科研项目')
        type = 'program'
      else if (indicatorType === '科技奖')
        type = 'award'
      else if (indicatorType === '决策咨询成果')
        type = 'decision'
      this.remove(id,type)
    },
    importData(){},

    // 每页显示的条数
    handleSizeChange(val) {
      // 改变每页显示的条数
      this.PageSize=val
      // 注意：在改变每页显示的条数时，要将页码显示到第一页
      this.currentPage=1
      this.getTableByYear(this.indicatorID,this.year)
    },
    // 显示第几页
    handleCurrentChange(val) {
      // 改变默认的页数
      this.currentPage=val;
      this.getTableByYear(this.indicatorID,this.year)
    },
    readFile(file) {//文件读取
      return new Promise(resolve => {
        let reader = new FileReader();
        reader.readAsBinaryString(file);//以二进制的方式读取
        reader.onload = ev => {
          resolve(ev.target.result);
        }
      })
    },
    async handleAdd(ev) { //批量添加上传与前端解析
      let file = ev.raw;
      if (!file) {
        alert("上传失败！")
        return;
      } else {
        let data = await this.readFile(file);
        let workbook = XLSX.read(data, {type: "binary"});//解析二进制格式数据
        var results = {}
        for(var i = 0;i<workbook.SheetNames.length;i++){
          const firstSheetName = workbook.SheetNames[i];
          results[firstSheetName] = []
          const worksheet = workbook.Sheets[firstSheetName];
          if (typeof worksheet.A1 != "undefined" && (worksheet.A2.v != '《东华大学计算机学报》'|| typeof worksheet.A3 != "undefined")) { //判断一下有没有空表 或者空白表格
            results[firstSheetName] = [...results[firstSheetName],...XLSX.utils.sheet_to_json(worksheet)]
          }
        }
        for(var i = 0;i<workbook.SheetNames.length;i++){//比如有6种论文类别
          const firstSheetName = workbook.SheetNames[i];
          if(results[firstSheetName].length){
            if (results[firstSheetName][0]["刊物全称"] == "《东华大学计算机学报》")
              results[firstSheetName].shift()
          }
          for(var j = 0;j < results[firstSheetName].length;j ++){
            if(results[firstSheetName].length){
              results[firstSheetName][j]['所属类别'] = firstSheetName
              if (typeof results[firstSheetName][j]["刊物全称"] == "undefined")
                this.uploadResultError = true
            }
          }
          if(results[firstSheetName].length){//该类别数据不为空
            this.tableUploadData = [...this.tableUploadData,...results[firstSheetName]]
          }
        }
        if (!this.uploadResultError){
          this.uploadResultValid = true
        }
        this.uploadResult = true
        // console.log(this.tableUploadData)
      //   this.tableUploadData = XLSX.utils.sheet_to_json(worksheet);//json数据格式
      //   console.log(this.tableUploadData)
      //   for(let i = 0; i < this.tableUploadData.length; i++)
      //   {
      //     if (typeof this.tableUploadData[i]['刊物全称'] === 'undefined')
      //       this.errorRow.push(i+2)
      //   }
      //   if (this.errorRow.length === 0)
      //     this.uploadResultValid = true
      //   this.uploadResult = true
      }
    },
    handleClose(){ //批量添加关闭
      this.uploadResult = false;
      this.fileList = []
      this.tableUploadData = []
      this.errorRow = []
      this.uploadResultValid = false;
    },
    uploadConfirm(){//确认上传导入的文件
      var that = this
      this.$confirm('是否确定添加'+this.tableUploadData.length+'条记录，批量导入后将覆盖所有'+this.year+"年的数据", '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {//点击确认
        this.uploadAppendPublication().then(
            () => {
              that.$message({
                    type: 'success',
                    message: '添加成功'
                  },
              )
              that.uploadVisible = false
              that.handleClose()
              // that.getTableByYear(that.indicatorID,that.year)
            }
        )
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消'
        });
      });
    },
    async uploadAppendPublication(){
      await this.appendPublicationAsync()
    },

    //导出excel模版按钮
    btnClickExport() {
      var sheetTitlesAndId = []//保存所有sheet信息
      var tableSample = []//sheet表头信息 标题
      if(this.importSelectType == '论文'){
          this.typeOfAllPaper.forEach((item) => {
            sheetTitlesAndId.push(item)
          })
        tableSample = this.tablePaperSample
      }else if(this.importSelectType == '科技奖'){
        this.typeOfAllTechnical.forEach((item) => {
          sheetTitlesAndId.push(item)
        })
        tableSample = this.tableTechnicalSample
      }else if(this.importSelectType == '咨询决策成果'){
        this.typeOfAllDecision.forEach((item) => {
          sheetTitlesAndId.push(item)
        })
        tableSample = this.tableDecisionSample

      }else if(this.importSelectType == '纵向科研成果'){
        this.typeOfAllProject.forEach((item) => {
          sheetTitlesAndId.push(item)
        })
        tableSample = this.tableProjectSample
      }
      // console.log(sheetTitlesAndId)
      var wb = XLSX.utils.book_new();
      for(var i = 0;i<sheetTitlesAndId.length;i ++){
        var sheet = XLSX.utils.json_to_sheet(tableSample);//设置每个sheet的表头标题
        console.log(sheet)
        XLSX.utils.book_append_sheet(wb, sheet, sheetTitlesAndId[i].label);
      }
      console.log(wb)
      const workbookBlob = this.workbook2blob(wb);
      this.openDownloadDialog(workbookBlob, '模版.xlsx');
    },
    workbook2blob(workbook) {
      // 生成excel的配置项
      var wopts = {
        // 要生成的文件类型
        bookType: "xlsx",
        // 是否生成Shared String Table，官方解释是，如果开启生成速度会下降，但在低版本IOS设备上有更好的兼容性
        bookSST: false,
        type: "binary"
      };
      var wbout = XLSX.write(workbook, wopts);
      // 将字符串转ArrayBuffer
      function s2ab(s) {
        var buf = new ArrayBuffer(s.length);
        var view = new Uint8Array(buf);
        for (var i = 0; i != s.length; ++i) view[i] = s.charCodeAt(i) & 0xff;
        return buf;
      }
      var blob = new Blob([s2ab(wbout)], {
        type: "application/octet-stream"
      });
      return blob;
    },
    openDownloadDialog(blob, fileName) {
      if (typeof blob == "object" && blob instanceof Blob) {
        blob = URL.createObjectURL(blob); // 创建blob地址
      }
      var aLink = document.createElement("a");
      aLink.href = blob;
      // HTML5新增的属性，指定保存文件名，可以不要后缀，注意，有时候 file///模式下不会生效
      aLink.download = fileName || "";
      var event;
      if (window.MouseEvent) event = new MouseEvent("click");
      //   移动端
      else {
        event = document.createEvent("MouseEvents");
        event.initMouseEvent("click", true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
      }
      aLink.dispatchEvent(event);
    },
    downloadExcel(idname) {
      // let tables = document.getElementById(idname);
      // let table_book = XLSX.utils.table_to_book(tables);
      //
      // var table_write = XLSX.write(table_book, {
      //   bookType: "xlsx",
      //   bookSST: true,
      //   type: "array",
      // });
      // try {
      //   FileSaver.saveAs(
      //       new Blob([table_write], { type: "application/octet-stream" }),
      //       "模版.xlsx"
      //   );
      // } catch (e) {
      //   if (typeof console !== "undefined") console.log(e, table_write);
      // }
      // return table_write;
    },
    checkUploadData({row, rowIndex}){
      if (typeof row['刊物全称'] === 'undefined') {
        return 'warning-row';
      }
      return '';
    }
  },
}

</script>

<style>

.select_div_input{
  margin-left:3px;
  width:90%;
  height:35px;
  position:relative;
  display:inline-block
}
.select_div{
  border: .5px solid #fff;
  border-radius: 3px;
  margin-top: 5px;
  font-size: 14px;
  position: absolute;
  background-color: #fff;
  z-index: 999;
  overflow: auto;
  width: 300px;
  cursor: pointer;
}
.select_div_div:hover{
  color: blue;
}
.select_div_div{
  padding-bottom: 7px;
  padding-top: 7px;
  padding-left: 12px;
  width: 100%;
}
.el-form-item__label-wrap{
  margin-left: 0px !important;
}
.el-table.el-table__fixed-right{
  top: 0px;
  left: auto;
  right: 0px;
  border-left: 1px solid rgb(223,230,236);
  box-sizing: content-box;
}
.el-table .warning-row {
  background: oldlace;

}
</style>