<template>
  <div>
    <el-breadcrumb
        separator-class="el-icon-arrow-right"
        style="float: left; margin-top: 10px; font-size: 15px"
        v-show="isRoot"
    >
      <el-breadcrumb-item style="margin-left: 5px">
        {{ p1 }}
      </el-breadcrumb-item>
      <el-breadcrumb-item
      >{{ category }}（{{ indicatorTypeZH }}）：{{ score }}分
      </el-breadcrumb-item>
    </el-breadcrumb>
    <!--    选择年份-->
    <el-select
        style="float: left; margin-left: 20px; width: 80px"
        v-model="year"
        v-show="isRoot"
        @change="changeYear"
    >
      <el-option
          v-for="year in yearList"
          :key="year"
          :value="year"
          :label="year"
      ></el-option>
    </el-select>
    <div style="float: left; margin-top: 4px; margin-left: 3px" v-show="isRoot">
      年
    </div>
    <div
        style="text-align: center; margin-top: 100px; font-size: 30px"
        v-show="!isRoot"
    >
      请从左边选择一个分类
    </div>
    <!--添加-->
    <div
        style="padding-bottom: 40px; margin-top: 5px; margin-right: 10px"
        v-show="isRoot"
    >
      <el-button
          type="primary"
          style="float: right; margin-left: 10px"
          @click="
          () => {
            publicationInf.year = year;
            if (indicatorType === 'publication')
              this.dialogVisibleAppendPublication = true;
            else if (indicatorType === 'award'){
              this.awardInf.year = new Date().getFullYear()
              this.dialogVisibleAppendAward = true;
            }
            else if (indicatorType === 'project'){
             this.programInf.year = new Date().getFullYear()
             this.dialogVisibleAppendProgram = true;
            }
            else if (indicatorType === 'decision'){
             this.decisionInf.year = new Date().getFullYear()
             this.dialogVisibleAppendDecision = true;
            }
            else if (indicatorType === 'competition'){
             this.competitionInf.year = new Date().getFullYear()
             this.dialogVisibleAppendCompetition = true;
            }
          }
        "
          icon="el-icon-circle-plus"
      >添加
      </el-button>
      <el-button
          type="primary"
          style="float: right; margin-left: 10px"
          @click="
          uploadVisible = true;
          importSelectYear = year;
          uploadResultError = false;
        "
          icon="el-icon-s-order"
      >批量导入
      </el-button>
      <el-button
          type="primary"
          style="float: right; margin-left: 10px"
          @click="dialogVisibleClone = true"
          icon="el-icon-s-order"
      >克隆
      </el-button>
      <el-button
          @click="
          searchUnAvailable = false;
          dialogVisibleSearch = true;
        "
          type="primary"
          style="float: right"
          icon="el-icon-search"
      >搜索
      </el-button>
    </div>
    <!--    点击克隆按钮-->
    <el-dialog
        :visible.sync="dialogVisibleClone"
        width="90%"
        @open="getYearList(year)"
    >
      <span slot="title" style="text-align: center; font-size: 20px">克隆</span>
      <div>
        从
        <el-select
            v-model="fromYear"
            placeholder="选择年份"
            style="width: 120px"
        >
          <!-- 在这里添加下拉框选项 -->
          <el-option
              v-for="year in yearList"
              :key="year"
              :value="year"
              :label="year"
          ></el-option>
        </el-select>
        年克隆到
        <el-input-number v-model="toYear" :min="1999" :max="new Date().getFullYear()"
                         style="width: 120px"></el-input-number>
        年
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="closeClone()">关 闭</el-button>
        <el-button type="primary" @click="clone()">确 定</el-button>
      </span>
    </el-dialog>
    <!--    点击搜索按钮-->
    <el-dialog
        :visible.sync="dialogVisibleSearch"
        @close="closeSearch"
        width="90%"
    >
      <span slot="title" style="float: left; font-size: 20px"
      >请输入需要搜索的名称</span
      >
      <div style="margin-bottom: 10px">
        <span>请选择搜索的类别：</span>
        <el-select
            style="margin-right: 20px; width: 120px"
            v-model="searchSelectType"
            @change="handleOptionChange"
        >
          <el-option
              v-for="item in selectTypes"
              :key="item.value"
              :label="item.label"
              :value="item.value"
          >
          </el-option>
        </el-select>
      </div>

      <div class="select_div_input">
        <input
            autocomplete="off"
            style="
            margin-left: 0px;
            width: 30%;
            line-height: 28px;
            border: 1px solid lightgrey;
            padding: 0 10px 1px 15px;
            border-radius: 4px;
            color: gray;
          "
            placeholder="请输入名称"
            v-model="publicationName"
            @focus="ispubShow = true"
            @blur="ispubShow = ispubFlag"
            id="input_publicationName"
        />
        <div
            class="select_div"
            v-show="ispubShow && publicationName ? true : false"
            :style="'height:${menuHeight}'"
            @mouseover="ispubFlag = true"
            @mouseleave="ispubFlag = false"
        >
          <div
              class="select_div_div"
              v-for="(val, idx) in select_pubName"
              :key="idx"
              :value="val.value"
              @click="filter_pub(val.value)"
          >
            {{ val.value }}
          </div>
        </div>
      </div>

      <el-table
          v-if="searchPathInf.type === 'publication'"
          :data="listSearchPublicationsByName"
          border
          style="width: 100%"
      >
        <el-table-column fixed prop="indicatorName" label="指标点名称">
        </el-table-column>
        <el-table-column
            fixed
            width="50px"
            prop="score"
            align="center"
            label="分值"
        >
        </el-table-column>
        <el-table-column fixed prop="name" label="刊物全称"></el-table-column>
        <el-table-column prop="abbr" label="刊物简称"></el-table-column>
        <el-table-column prop="publisher" label="出版社"></el-table-column>
        <el-table-column prop="url" label="刊物网址"></el-table-column>
        <el-table-column
            prop="year"
            label="录入年份"
            align="center"
            width="70px"
        >
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="150">
          <template slot-scope="scope">
            <el-button
                @click="
                rowData = JSON.parse(JSON.stringify(scope.row));
                dialogVisibleUpdatePublication = true;
              "
                size="mini"
            >编辑
            </el-button>
            <el-button
                @click="remove(scope.row.id, indicatorType)"
                type="danger"
                size="mini"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-table
          v-if="searchPathInf.type === 'patent'"
          :data="listSearchPublicationsByName"
          border
          style="width: 100%"
      >
        <el-table-column fixed prop="indicatorName" label="指标点名称">
        </el-table-column>
        <el-table-column
            fixed
            width="50px"
            prop="score"
            align="center"
            label="分值"
        >
        </el-table-column>
        <el-table-column fixed prop="name" label="类别名"></el-table-column>
        <el-table-column
            prop="year"
            label="录入年份"
            align="center"
            width="70px"
        >
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="150">
          <template slot-scope="scope">
            <el-button
                @click="
                rowData = JSON.parse(JSON.stringify(scope.row));
                dialogVisibleUpdatePublication = true;
              "
                size="mini"
            >编辑
            </el-button>
            <el-button
                @click="remove(scope.row.id, indicatorType)"
                type="danger"
                size="mini"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-table
          v-if="searchPathInf.type === 'award'"
          :data="listSearchPublicationsByName"
          border
          style="width: 100%"
      >
        <el-table-column fixed prop="indicatorName" label="指标点名称">
        </el-table-column>
        <el-table-column
            fixed
            width="50px"
            prop="score"
            align="center"
            label="分值"
        >
        </el-table-column>
        <el-table-column fixed prop="name" label="刊物全称"></el-table-column>
        <el-table-column prop="abbr" label="刊物简称"></el-table-column>
        <el-table-column prop="publisher" label="出版社"></el-table-column>
        <el-table-column prop="url" label="刊物网址"></el-table-column>
        <el-table-column
            prop="year"
            label="录入年份"
            align="center"
            width="70px"
        >
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="150">
          <template slot-scope="scope">
            <el-button
                @click="
                rowData = JSON.parse(JSON.stringify(scope.row));
                dialogVisibleUpdatePublication = true;
              "
                size="mini"
            >编辑
            </el-button>
            <el-button
                @click="remove(scope.row.id, indicatorType)"
                type="danger"
                size="mini"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-table
          v-if="searchPathInf.type === 'project'"
          :data="listSearchPublicationsByName"
          border
          style="width: 100%"
      >
        <el-table-column fixed prop="indicatorName" label="指标点名称">
        </el-table-column>
        <el-table-column
            fixed
            width="50px"
            prop="score"
            align="center"
            label="分值"
        >
        </el-table-column>
        <el-table-column fixed prop="name" label="类别名"></el-table-column>
        <el-table-column
            prop="year"
            label="录入年份"
            align="center"
            width="70px"
        >
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="150">
          <template slot-scope="scope">
            <el-button
                @click="
                rowData = JSON.parse(JSON.stringify(scope.row));
                dialogVisibleUpdatePublication = true;
              "
                size="mini"
            >编辑
            </el-button>
            <el-button
                @click="remove(scope.row.id, indicatorType)"
                type="danger"
                size="mini"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-table
          v-if="searchPathInf.type === 'standard'"
          :data="listSearchPublicationsByName"
          border
          style="width: 100%"
      >
        <el-table-column fixed prop="indicatorName" label="指标点名称">
        </el-table-column>
        <el-table-column
            fixed
            width="50px"
            prop="score"
            align="center"
            label="分值"
        >
        </el-table-column>
        <el-table-column fixed prop="name" label="刊物全称"></el-table-column>
        <el-table-column prop="abbr" label="刊物简称"></el-table-column>
        <el-table-column prop="publisher" label="出版社"></el-table-column>
        <el-table-column prop="url" label="刊物网址"></el-table-column>
        <el-table-column
            prop="year"
            label="录入年份"
            align="center"
            width="70px"
        >
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="150">
          <template slot-scope="scope">
            <el-button
                @click="
                rowData = JSON.parse(JSON.stringify(scope.row));
                dialogVisibleUpdatePublication = true;
              "
                size="mini"
            >编辑
            </el-button>
            <el-button
                @click="remove(scope.row.id, indicatorType)"
                type="danger"
                size="mini"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-table
          v-if="searchPathInf.type === 'decision'"
          :data="listSearchPublicationsByName"
          border
          style="width: 100%"
      >
        <el-table-column fixed prop="indicatorName" label="指标点名称">
        </el-table-column>
        <el-table-column
            fixed
            width="50px"
            prop="score"
            align="center"
            label="分值"
        >
        </el-table-column>
        <el-table-column fixed prop="name" label="类别名"></el-table-column>
        <el-table-column
            prop="year"
            label="录入年份"
            align="center"
            width="70px"
        >
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="150">
          <template slot-scope="scope">
            <el-button
                @click="
                rowData = JSON.parse(JSON.stringify(scope.row));
                dialogVisibleUpdatePublication = true;
              "
                size="mini"
            >编辑
            </el-button>
            <el-button
                @click="remove(scope.row.id, indicatorType)"
                type="danger"
                size="mini"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-table
          v-if="searchPathInf.type === 'book'"
          :data="listSearchPublicationsByName"
          border
          style="width: 100%"
      >
        <el-table-column fixed prop="indicatorName" label="指标点名称">
        </el-table-column>
        <el-table-column
            fixed
            width="50px"
            prop="score"
            align="center"
            label="分值"
        >
        </el-table-column>
        <el-table-column fixed prop="name" label="刊物全称"></el-table-column>
        <el-table-column prop="abbr" label="刊物简称"></el-table-column>
        <el-table-column prop="publisher" label="出版社"></el-table-column>
        <el-table-column prop="url" label="刊物网址"></el-table-column>
        <el-table-column
            prop="year"
            label="录入年份"
            align="center"
            width="70px"
        >
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="150">
          <template slot-scope="scope">
            <el-button
                @click="
                rowData = JSON.parse(JSON.stringify(scope.row));
                dialogVisibleUpdatePublication = true;
              "
                size="mini"
            >编辑
            </el-button>
            <el-button
                @click="remove(scope.row.id, indicatorType)"
                type="danger"
                size="mini"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-table
          v-if="searchPathInf.type === 'application'"
          :data="listSearchPublicationsByName"
          border
          style="width: 100%"
      >
        <el-table-column fixed prop="indicatorName" label="指标点名称">
        </el-table-column>
        <el-table-column
            fixed
            width="50px"
            prop="score"
            align="center"
            label="分值"
        >
        </el-table-column>
        <el-table-column fixed prop="name" label="类别名"></el-table-column>
        <el-table-column
            prop="year"
            label="录入年份"
            align="center"
            width="70px"
        >
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="150">
          <template slot-scope="scope">
            <el-button
                @click="
                rowData = JSON.parse(JSON.stringify(scope.row));
                dialogVisibleUpdatePublication = true;
              "
                size="mini"
            >编辑
            </el-button>
            <el-button
                @click="remove(scope.row.id, indicatorType)"
                type="danger"
                size="mini"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-table
          v-if="searchPathInf.type === 'competition'"
          :data="listSearchPublicationsByName"
          border
          style="width: 100%"
      >
        <el-table-column fixed prop="indicatorName" label="指标点名称">
        </el-table-column>
        <el-table-column
            fixed
            width="50px"
            prop="score"
            align="center"
            label="分值"
        >
        </el-table-column>
        <el-table-column fixed prop="name" label="刊物全称"></el-table-column>
        <el-table-column prop="abbr" label="刊物简称"></el-table-column>
        <el-table-column prop="publisher" label="出版社"></el-table-column>
        <el-table-column prop="url" label="刊物网址"></el-table-column>
        <el-table-column
            prop="year"
            label="录入年份"
            align="center"
            width="70px"
        >
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="150">
          <template slot-scope="scope">
            <el-button
                @click="
                rowData = JSON.parse(JSON.stringify(scope.row));
                dialogVisibleUpdatePublication = true;
              "
                size="mini"
            >编辑
            </el-button>
            <el-button
                @click="remove(scope.row.id, indicatorType)"
                type="danger"
                size="mini"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button
            type="danger"
            v-show="pathVisible"
            @click="searchDelete(searchInf2.id, searchPathInf.type)"
        >删除</el-button
        >
        <el-button
            type="primary"
            v-show="pathVisible"
            @click="
            dialogVisibleSearch = false;
            searchUpdate(searchPathInf.type);
          "
        >修改</el-button
        >
        <el-button @click="closeSearch()">关 闭</el-button>
      </span>
    </el-dialog>

    <!--批量导入-->
    <el-dialog :visible.sync="uploadVisible" @close="handleClose" width="80%">
      <span slot="title" style="float: left; font-size: 25px"
      >请上传需要导入的文件</span
      >
      <span>请选择导入的年份：</span>
        <el-input-number v-model="importSelectYear" :min="1999" :max="new Date().getFullYear()" v-show="isRoot"
                         style="width: 120px"></el-input-number>
        年
      <div>
        <div style="margin-left: 10px;margin-top: 10px">第一步：
          <el-button icon="el-icon-upload" type="primary" style="margin-right: 10px" @click="btnClickExport">下载模版
          </el-button>
        </div>
      </div>
      <span
          style="float: right; font-size: 16px; color: red"
          v-show="uploadResultError"
      >{{errorMessage}}</span
      >
      <span
          style="float: right; font-size: 16px; color: green"
          v-show="uploadResultValid"
      >数据校验通过</span
      >
      <div style="margin-left: 10px">
        <el-upload
          :file-list="fileList"
          :auto-upload="false"
          accept=".xlsx, .xls"
          :on-change="handleAdd"
          :on-remove="handleClose"
      >
        <div style="margin-top: 10px">第二步：
          <el-button icon="el-icon-plus" type="success">点击上传</el-button>
        </div>
      </el-upload>
      </div>

      <el-table
          v-show="uploadResult"
          :data="tableUploadData"
          :row-class-name="checkUploadData"
          style="width: 100%"
      >
        <el-table-column prop="刊物全称" label="刊物全称" v-if="indicatorTypeZH=='学术论文'"></el-table-column>
        <el-table-column prop="刊物简称" label="刊物简称" v-if="indicatorTypeZH=='学术论文'"></el-table-column>
        <el-table-column prop="出版社" label="出版社" v-if="indicatorTypeZH=='学术论文'"></el-table-column>
        <el-table-column prop="网址" label="网址" v-if="indicatorTypeZH=='学术论文'"></el-table-column>
        <el-table-column prop="收录级别 （不同收录级别请用分号隔开）" label="收录级别 （不同收录级别请用分号隔开）" v-if="indicatorTypeZH=='学术论文'"></el-table-column>
        <el-table-column prop="项目名称" label="项目名称" v-if="indicatorTypeZH=='科研项目'"></el-table-column>
        <el-table-column prop="奖项名称" label="奖项名称" v-if="indicatorTypeZH=='科研获奖'"></el-table-column>
        <el-table-column prop="成果名称" label="成果名称" v-if="indicatorTypeZH=='决策咨询'"></el-table-column>
      </el-table>

      <span slot="footer" class="dialog-footer">
        <el-button
            type="primary"
            v-show="uploadResultValid === true"
            @click="uploadConfirm()"
        >确认</el-button
        >
        <el-button
            @click="
            uploadVisible = false;
            handleClose();
          "
        >关 闭</el-button
        >
      </span>
    </el-dialog>

   <!--期刊-->
    <el-table
        v-if="indicatorType === 'publication'"
        :data="tableData"
        border
        :header-cell-style="{ textAlign: 'center' }"
        style="width: 100%"
    >
      <el-table-column prop="name" width="200px" label="刊物全称">
      </el-table-column>
      <el-table-column prop="abbr" width="100px" label="刊物简称">
      </el-table-column>
      <el-table-column prop="publisher" label="出版社"></el-table-column>
      <el-table-column
          prop="url"
          width="200px"
          label="刊物网址"
      ></el-table-column>
      <!--      <el-table-column prop="year" width="70px" label="录入年份" align="center">-->
      <!--      </el-table-column>-->
      <el-table-column fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button
              @click="
              rowData = JSON.parse(JSON.stringify(scope.row));
              dialogVisibleUpdatePublication = true;
            "
              size="mini"
          >编辑
          </el-button>
          <el-button
              @click="remove(scope.row.id, indicatorType)"
              type="danger"
              size="mini"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--新增-->
    <el-dialog
        :title="titleAddPublication"
        :visible.sync="dialogVisibleAppendPublication"
        width="30%"
        center
    >
      <el-form
          :model="publicationInf"
          :hide-required-asterisk="true"
          :label-position="labelPosition"
          label-width="180px"
      >
        <el-form-item label="期刊全称" label-width="90px">
          <el-input v-model="publicationInf.name"></el-input>
        </el-form-item>
        <el-form-item label="刊物简称" label-width="90px">
          <el-input v-model="publicationInf.abbr"></el-input>
        </el-form-item>
        <el-form-item label="出版社" label-width="90px">
          <el-input v-model="publicationInf.publisher"></el-input>
        </el-form-item>
        <el-form-item label="网址" label-width="90px">
          <el-input v-model="publicationInf.url"></el-input>
        </el-form-item>
        <el-form-item label="录入年份" label-width="90px">
          <el-input
              v-model="publicationInf.year"
              :placeholder="nowYear"
          ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleAppendPublication = false"
        >取 消</el-button
        >
        <el-button
            type="primary"
            @click="
            dialogVisibleAppendPublication = false;
            appendPublication();
          "
        >确 定</el-button
        >
      </span>
    </el-dialog>
    <!--编辑-->
    <el-dialog :visible.sync="dialogVisibleUpdatePublication" width="30%">
      <span slot="title" style="float: left; font-size: 20px"
      >请输入期刊的相关信息</span
      >
      <el-form :model="rowData">
        <el-form-item label="期刊全称">
          <el-input v-model="rowData.name"></el-input>
        </el-form-item>
        <el-form-item label="刊物简称">
          <el-input v-model="rowData.abbr"></el-input>
        </el-form-item>
        <el-form-item label="出版社">
          <el-input v-model="rowData.publisher"></el-input>
        </el-form-item>
        <el-form-item label="网址">
          <el-input v-model="rowData.url"></el-input>
        </el-form-item>
       <el-form-item label="录入年份">
        <el-input v-model="rowData.year"></el-input>
       </el-form-item>
        <!--        <el-form-item label="录入年份">-->
        <!--          <el-input v-model="rowData.year"></el-input>-->
        <!--        </el-form-item>-->
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUpdatePublication = false"
        >取 消</el-button
        >
        <el-button
            type="primary"
            @click="
            update(indicatorType);
            dialogVisibleUpdatePublication = false;
          "
        >确 定</el-button
        >
      </span>
    </el-dialog>

    <!--科研获奖-->
    <el-table
        v-if="indicatorType === 'award'"
        :data="tableData"
        border
        style="width: 100%"
    >
      <el-table-column fixed prop="name" label="奖项名"></el-table-column>


      <el-table-column fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button
              @click="
              rowData = JSON.parse(JSON.stringify(scope.row));
              dialogVisibleUpdateAward = true;
            "
          >编辑
          </el-button>
          <el-button
              @click="remove(scope.row.id, indicatorType)"
              type="danger"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--新增-->
    <el-dialog :visible.sync="dialogVisibleAppendAward" width="30%">
      <span slot="title" style="float: left; font-size: 20px"
      >请输入科研获奖的相关信息</span
      >
      <el-form :model="awardInf">
        <el-form-item label="奖项名">
          <el-input v-model="awardInf.name"></el-input>
        </el-form-item>
       <el-form-item label="录入年份">
        <el-input v-model="awardInf.year"></el-input>
       </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleAppendAward = false">取 消</el-button>
        <el-button
            type="primary"
            @click="
            appendAward();
            dialogVisibleAppendAward = false;
          "
        >确 定</el-button
        >
      </span>
    </el-dialog>
    <!--编辑-->
    <el-dialog :visible.sync="dialogVisibleUpdateAward" width="30%">
      <span slot="title" style="float: left; font-size: 20px"
      >请输入科技奖的相关信息</span
      >
      <el-form :model="rowData">
        <el-form-item label="奖项名">
          <el-input v-model="rowData.name"></el-input>
        </el-form-item>
       <el-form-item label="录入年份">
        <el-input v-model="rowData.year"></el-input>
       </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUpdateAward = false">取 消</el-button>
        <el-button
            type="primary"
            @click="
            update(indicatorType);
            dialogVisibleUpdateAward = false;
          "
        >确 定</el-button
        >
      </span>
    </el-dialog>

    <!--科研项目-->
    <el-table
        v-if="indicatorType === 'project'"
        :data="tableData"
        border
        style="width: 100%"
    >
      <el-table-column fixed prop="name" label="项目名"></el-table-column>
      <el-table-column fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button
              @click="
              rowData = JSON.parse(JSON.stringify(scope.row));
              dialogVisibleUpdateProgram = true;
            "
              size="small"
          >编辑
          </el-button>
          <el-button
              @click="remove(scope.row.id, indicatorType)"
              type="danger"
              size="small"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--新增-->
    <el-dialog :visible.sync="dialogVisibleAppendProgram" width="40%">
      <span slot="title" style="float: left; font-size: 20px"
      >请输入科研项目类别的的相关信息</span
      >
      <el-form :model="programInf">
        <el-form-item label="类别名">
          <el-input v-model="programInf.name"></el-input>
        </el-form-item>
       <el-form-item label="录入年份">
        <el-input v-model="programInf.year"></el-input>
       </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleAppendProgram = false">取 消</el-button>
        <el-button
            type="primary"
            @click="
            appendProgram();
            dialogVisibleAppendProgram = false;
          "
        >确 定</el-button
        >
      </span>
    </el-dialog>
    <!--编辑-->
    <el-dialog :visible.sync="dialogVisibleUpdateProgram" width="30%">
      <span slot="title" style="float: left; font-size: 20px"
      >请输入科研项目的相关信息</span
      >
      <el-form :model="rowData">
        <el-form-item label="科研项目名">
          <el-input v-model="rowData.name"></el-input>
        </el-form-item>
       <el-form-item label="录入年份">
        <el-input v-model="rowData.year"></el-input>
       </el-form-item>
      </el-form>
      <!--      <el-form :model="rowData">-->
      <!--        <el-form-item label="录入年份">-->
      <!--          <el-input v-model="rowData.year"></el-input>-->
      <!--        </el-form-item>-->
      <!--      </el-form>-->
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUpdateProgram = false">取 消</el-button>
        <el-button
            type="primary"
            @click="
            update(indicatorType);
            dialogVisibleUpdateProgram = false;
          "
        >确 定</el-button
        >
      </span>
    </el-dialog>

    <!--决策咨询成果-->
    <el-table
        v-if="indicatorType === 'decision'"
        :data="
        tableData
      "
        border
        style="width: 100%"
    >
      <el-table-column fixed prop="name" label="决策咨询成果名">
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button
              @click="
              rowData = JSON.parse(JSON.stringify(scope.row));
              dialogVisibleUpdateDecision = true;
            "
              size="small"
          >编辑
          </el-button>
          <el-button
              @click="remove(scope.row.id, 'decision')"
              type="danger"
              size="small"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--新增-->
    <el-dialog :visible.sync="dialogVisibleAppendDecision" width="30%">
      <span slot="title" style="float: left; font-size: 20px"
      >请输入决策咨询成果的相关信息</span
      >
      <el-form :model="decisionInf">
        <el-form-item label="决策咨询成果名">
          <el-input v-model="decisionInf.name"></el-input>
        </el-form-item>
       <el-form-item label="录入年份">
        <el-input v-model="decisionInf.year"></el-input>
       </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleAppendDecision = false"
        >取 消</el-button
        >
        <el-button
            type="primary"
            @click="
            appendDecision();
            dialogVisibleAppendDecision = false;
          "
        >确 定</el-button
        >
      </span>
    </el-dialog>
    <!--编辑-->
    <el-dialog :visible.sync="dialogVisibleUpdateDecision" width="30%">
      <span slot="title" style="float: left; font-size: 20px"
      >请输入决策咨询成果的相关信息</span
      >
      <el-form :model="rowData">
        <el-form-item label="决策咨询成果名">
          <el-input v-model="rowData.name"></el-input>
        </el-form-item>
       <el-form-item label="录入年份">
        <el-input v-model="rowData.year"></el-input>
       </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUpdateDecision = false"
        >取 消</el-button
        >
        <el-button
            type="primary"
            @click="
            update(indicatorType);
            dialogVisibleUpdateDecision = false;
          "
        >确 定</el-button
        >
      </span>
    </el-dialog>

   <!--学科竞赛-->
   <el-table
       v-if="indicatorType === 'competition'"
       :data="
        tableData
      "
       border
       style="width: 100%"
   >
    <el-table-column fixed prop="name" label="学科竞赛名">
    </el-table-column>
    <el-table-column fixed="right" label="操作" width="150">
     <template slot-scope="scope">
      <el-button
          @click="
              rowData = JSON.parse(JSON.stringify(scope.row));
              dialogVisibleUpdateCompetition = true;
            "
          size="small"
      >编辑
      </el-button>
      <el-button
          @click="remove(scope.row.id, 'competition')"
          type="danger"
          size="small"
      >删除
      </el-button>
     </template>
    </el-table-column>
   </el-table>
   <!--新增-->
   <el-dialog :visible.sync="dialogVisibleAppendCompetition" width="30%">
      <span slot="title" style="float: left; font-size: 20px"
      >请输入学科竞赛的相关信息</span
      >
    <el-form :model="competitionInf">
     <el-form-item label="学科竞赛成果名">
      <el-input v-model="competitionInf.name"></el-input>
     </el-form-item>
     <el-form-item label="录入年份">
      <el-input v-model="competitionInf.year"></el-input>
     </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleAppendCompetition = false"
        >取 消</el-button
        >
        <el-button
            type="primary"
            @click="
            appendCompetition();
            dialogVisibleAppendCompetition = false;
          "
        >确 定</el-button
        >
      </span>
   </el-dialog>
   <!--编辑-->
   <el-dialog :visible.sync="dialogVisibleUpdateCompetition" width="30%">
      <span slot="title" style="float: left; font-size: 20px"
      >请输入学科竞赛的相关信息</span
      >
    <el-form :model="rowData">
     <el-form-item label="学科竞赛名">
      <el-input v-model="rowData.name"></el-input>
     </el-form-item>
     <el-form-item label="录入年份">
      <el-input v-model="rowData.year"></el-input>
     </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisibleUpdateCompetition = false"
        >取 消</el-button
        >
        <el-button
            type="primary"
            @click="
            update(indicatorType);
            dialogVisibleUpdateCompetition = false;
          "
        >确 定</el-button
        >
      </span>
   </el-dialog>

    <div style="margin-top: 10px; text-align: right" v-show="isRoot">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page.sync="currentPage"
          :page-sizes="pageSizes"
          :page-size="PageSize"
          layout="total, sizes, prev, pager, next, jumper"
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
import * as XLSX from "xlsx/xlsx.mjs";
import FileSaver from "file-saver";
import th from "element-ui/src/locale/lang/th";

export default {
  name: "search",
  props: ["category", "type", "order", "score", "p1", "p2"],
  data() {
    return {
      selectedOption: 1,
      titleAddPublication: "请添加期刊的相关信息",
      labelPosition: "left",
      typeOfAllPaper: [],
      typeOfAllProject: [],
      typeOfAllDecision: [],
      typeOfAllTechnical: [],
      listSearchPublicationsByName: [],
      ispubFlag: false,
      ispubShow: false,
      select_pubName: [],
      timer: null,
      publicationName: "", //搜索中要搜索的期刊
      searchSelectType: "",
      importSelectType: "",
      selectTypes: [
        {label: "学术论文", value: "publication"},
        {label: "科研获奖", value: "award"},
        {label: "科研项目", value: "project"},
        {label: "决策咨询", value: "decision"},
        {label: "学术专著和教材", value: "book"},
      ],
      importSelectYear: null,
      years: [],
      year: 0,
      fromYear: "",
      toYear: new Date().getFullYear(),
      yearList: [],
      nowYear: new Date().getFullYear(),
      tableData: [],
      uploadResult: false,
      uploadResultValid: false,
      uploadResultError: false,
      tableUploadData: [],
      fileList: [],
      ID: 0,
      indicatorID: 0,
      indicatorType: "",
      indicatorTypeZH: "",
      indicatorName: "",
      dialogVisibleAppendPublication: false,
      dialogVisibleUpdatePublication: false,
      dialogVisibleAppendAward: false,
      dialogVisibleUpdateAward: false,
      dialogVisibleAppendProgram: false,
      dialogVisibleUpdateProgram: false,
      dialogVisibleAppendDecision: false,
      dialogVisibleUpdateDecision: false,
      dialogVisibleUpdateCompetition: false,
      dialogVisibleAppendCompetition: false,
      dialogVisibleSearch: false,
      dialogVisibleClone: false,
      pathVisible: false,
      uploadVisible: false,
      errorMessage:'',
      rowData: "",
      searchInf: {}, //存放某个期刊（或其他）的indicatorID,名称,表格内id
      searchInf2: {}, //存放某个期刊（或其他）的具体信息
      searchResult: {}, //存放所有的结果
      searchPathInf: {}, //存放某个期刊（或其他）的目录信息(来自indicator)
      publicationInf: {
        name: "",
        abbr: "",
        publisher: "",
        url: "",
        level: "",
        year: "",
      },
      awardInf: {
        name: "",
        rankN: "",
        year:""
      },
      programInf: {
        name: "",
        year: new Date().getFullYear(),
      },
      decisionInf: {
        name: "",
       year: new Date().getFullYear(),
      },
     competitionInf: {
      name: "",
      year: new Date().getFullYear(),
     },
      isRoot: false,
      currentPage: 1,
      totalCount: 0,
      pageSizes: [10, 20, 50, 100],
      PageSize: 10,
      tablePaperSample: [
        //学术论文
        {
          刊物全称: "Artificial Intelligence",
          刊物简称: "AI",
          出版社: "Elsevier",
          网址: "http://dblp.uni-trier.de/db/journals/ai/",
          "收录级别 （不同收录级别请用分号隔开）": "SCI;",
        },
      ],
      tableTechnicalSample: [
        //科研获奖
        {
          奖项名称: "",
        },
      ],
      tableProjectSample: [
        //科研项目
        {
          项目名称: "",
        },
      ],
      tableDecisionSample: [
        //决策咨询
        {
          成果名称: "你的决策咨询成果名称（必填项）",
        },
      ],
      errorRow: [],
      searchUnAvailable: false,
    };
  },
  watch: {
    publicationName: {
      handler(val) {
        this.delaySelectInput(val);
      },
    },
  },
  mounted() {
    axios.get("/indicator").then((resp) => {
      //获得所有指标点
      var data = resp.obj[1];
      var that = this;
      data.forEach((item, idx1) => {
        if (item.children) {
          //大类 1 2 3
          // console.log(item)
          item.children.forEach((subItem, idx2) => {
            //大类下面的4个小类
            if (subItem.type == "学术论文") {
              //添加所有是论文类别的，所有大类
              that.typeOfAllPaper = [
                ...that.typeOfAllPaper,
                subItem,
              ];
            } else if (subItem.type == "科研项目") {
              that.typeOfAllProject = [
                ...that.typeOfAllProject,
                subItem,
              ];
            } else if (subItem.type == "科研获奖") {
              that.typeOfAllTechnical = [
                ...that.typeOfAllTechnical,
                subItem,
              ];
            } else if (subItem.type == "决策咨询") {  // 修改1
              that.typeOfAllDecision = [
                ...that.typeOfAllDecision,
                subItem,
              ];
            }
          });
        }
      });
      // console.log(that.typeOfAllPaper)
    });
  },
  methods: {
    handleRadioChange() {
      if (this.selectedOption === 0) {
        this.rowData.rankN = 0
      }
    },
    async getYearList(year) {
      try {
        const url = `/indicator/getAllYear/${this.indicatorID}/${this.indicatorType}`;
        await this.getRequest(url).then((resp) => {
          this.yearList = resp.obj;
          if (this.yearList.length > 0) {
            this.fromYear = this.yearList[0];
            this.year = year ? year : this.yearList[0];
          }
        });
      } catch (error) {
        this.$message.error("获取年份错误");
        // console.error(error);
      }
    },
    //克隆操作
    clone() {
      const currentYear = new Date().getFullYear();
      const minYear = 1999; // 最小年份
      const maxYear = currentYear; // 最大年份（可以根据实际需求进行调整）

      if(this.fromYear == ''){
        this.$message.info("被克隆的年份没有选择！");
        this.closeClone();
        return
      }


      if (this.toYear < minYear || this.toYear > maxYear) {
        // toYear 不在允许的范围内
        this.$message.error("年份不合法！");
        this.closeClone();
        return
      }
      if(this.fromYear==this.toYear){
        this.$message.warning("年份选择相同，请重新选择！");
        return
      }
      const fromYear = this.fromYear;
      const toYear = this.toYear;
      const indicatorId = this.indicatorID; // 替换为实际的indicatorId
      const indicatorType = this.indicatorType;
      const url = `/indicator/clone/${fromYear}/${toYear}/${indicatorId}/${indicatorType}`;

      this.postRequest(url).then((data) => {
        // 克隆操作成功的处理逻辑
        if (data.status == 200) {
          this.$message.success("克隆成功！");
          this.getTableByYear(
              this.indicatorID,
              this.year,
              this.indicatorType
          );
          this.getYearList(this.year);
        } else {
          this.$message.error("克隆失败！");
        }
        this.closeClone();
      });
    },
    handleOptionChange() {
      // console.log(this.searchSelectType);
      this.publicationName = "";
      this.searchPathInf.type = this.searchSelectType;
    },
    filter_pub(val) {
      this.listSearchPublicationsByName = [];
      this.select_pubName = [];
      this.publicationName = val;
      this.ispubFlag = false;
      this.ispubShow = false;
      if (!val) {
        return;
      }
      const url = `/indicator/getProductByTypeName?indicatorType=${encodeURIComponent(
          this.searchSelectType
      )}&fullName=${encodeURIComponent(val)}`;
      this.getRequest(url).then((resp) => {
        this.loading = false;
        if (resp && resp.obj.length !== 0) {
          resp.obj.forEach((item) => {
            const data = {name: item.name};
            if (this.searchPathInf.type == "publication") {
              data.abbr = item.abbr;
              data.id = item.id;
              data.indicatorName = item.indicatorName;
              data.publisher = item.publisher;
              data.url = item.url;
              data.year = item.year;
              data.score = item.score;
            } else if (this.searchPathInf.type == "project") {
              data.id = item.projectTypeId;
              data.indicatorName = item.indicatorName;
              data.name = item.projectTypeName;
              data.year = item.year;
              data.score = item.score;
            }
            this.listSearchPublicationsByName.push(data);
          });
        } else {
          this.$message.info("该类别未录入指标点中！请重新输入");
        }
        clearInterval(this.timer);
      });
    },

    delaySelectInput(val) {
      if (this.timer) {
        clearInterval(this.timer);
      }
      if (!val) {
        this.listSearchPublicationsByName = [];
        return;
      }
      const selectedType = this.selectTypes.find(item => item.value === this.searchSelectType);
      const publication = {
        type: selectedType ? selectedType.value : "",
        name: val
      };
      if (publication.type == "") {
        this.$message.error("成果类型选择错误！");
        return;
      }
      this.timer = setInterval(async () => {
        const url = `/indicator/getProductNamesByTypeName?indicatorType=${encodeURIComponent(publication.type)}&name=${encodeURIComponent(publication.name)}`;
        try {
          const resp = await this.getRequest(url);
          this.loading = false;
          if (resp && resp.obj != null) {
            this.select_pubName = resp.obj.map(item => ({value: item}));
          } else {
            this.select_pubName = [];
            this.ispubShow = false;
            this.$message.error(`无结果`);
          }
        } catch (error) {
          console.error(error);
        }
        clearInterval(this.timer);
      }, 300);
    },

    async getTableByYear(indicatorId, year, type, goLastPage) {
      try {
        const resp = await axios.get(
            `/indicator/getProductByYear?indicatorId=${indicatorId}&year=${year}&pageNum=${this.currentPage}&pageSize=${this.PageSize}&type=${type}`
        );
        if (resp.extend.res != null) {
          this.tableData = resp.extend.res[0];
          this.totalCount = resp.extend.res[1];
          if (goLastPage){
           this.handleCurrentChange(Math.ceil(this.totalCount / this.PageSize), true)
          }
        } else {
          this.tableData = [];
          this.totalCount = 0;
        }
      } catch (error) {
        console.error(error);
      }
    },

    changeYear() {
      this.getTableByYear(this.indicatorID, this.year, this.indicatorType);
    },
    remove(id, indicatorType) {
      let url = "";
      if (indicatorType === "publication") {
        url = `/publication?id=${id}&year=${this.year}`;
      } else if (indicatorType === "project") {
        url = `/projectType?id=${id}&year=${this.year}`;
      } else if (indicatorType === "award") {
        url = `/awardType?id=${id}&year=${this.year}`;
      }else if (indicatorType === "decision") {
        url = `/decisionType?id=${id}&year=${this.year}`;
      }else if (indicatorType === "competition") {
        url = `/competitionType?id=${id}`;
      }

      this.$confirm("确定要删除该条记录吗？", "提示", {type: "warning"})
          .then(() => {
            return axios.delete(url);
          })
          .then((resp) => {
            if (resp.status === 200) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              const index = this.tableData.findIndex((d) => d.id === id);
              this.tableData.splice(index, 1);
              this.totalCount--;
              this.handleCurrentChange(Math.ceil(this.totalCount / this.PageSize))
            } else {
              this.$message({
                type: "error",
                message: "删除失败!",
              });
            }
          })
          .finally(() => {
            this.closeSearch();
          });
    },
    async update(indicatorType) {
      try {
        const rowData = this.rowData;
        if (!rowData.name) {
          throw new Error("名称不能为空");
        }

        rowData.indicatorId = this.indicatorID;

        const paths = {
          project: "projectType",
          publication: "publication/basic/edit",
          award: "award/basic/awardType",
          decision:"decisionType",
          competition:"competitionType"
        };

        const url = `/${paths[indicatorType] || indicatorType}`;

        await axios.put(url, rowData).then((resp) => {
          if (resp.status === 200) {
            this.$message({
              type: "success",
              message: resp.msg,
            });
          }
        });
        await this.getTableByYear(this.indicatorID, this.year, this.indicatorType);
        await this.getYearList(this.year)
      } catch (error) {}
    },

    appendPublication() {
      if (this.publicationInf.name === "") {
        alert("期刊名称不能为空");
        this.dialogVisibleAppendPublication = true;
        return;
      }
      var postData = {
        name: this.publicationInf.name,
        abbr: this.publicationInf.abbr,
        publisher: this.publicationInf.publisher,
        url: this.publicationInf.url,
        indicatorId: this.indicatorID,
        year: this.publicationInf.year,
      };
      var that = this;
      this.postRequest1("/publication/basic/add", postData).then(function (resp) {
        that.getYearList(postData.year);

        that.getTableByYear(that.indicatorID, postData.year, that.indicatorType,true);
        if (resp.status != 200)
          that.$message({
            type: "error",
            message: "添加失败!",
          });
        else {
          that.$message({
            type: "success",
            message: resp.msg,
          });
          that.totalCount++;
         this.publicationInf = {
          name: "",
          abbr: "",
          publisher: "",
          url: "",
          level: "",
          year: "",
         }
        }
      });
    },
    appendPublicationAsync() {
      var token = localStorage.getItem("user")
          ? JSON.parse(localStorage.getItem("user")).token
          : "";
      var that = this;
      // var indicatorNames = [];
      //
      // for (var i = 0; i < this.tableUploadData.length; i++) {
      //   indicatorNames.push(this.tableUploadData[i]["所属类别"]);
      // }
      // var setIndicatorNames = new Set(indicatorNames); //去重
      // indicatorNames = [];
      // for (var val of setIndicatorNames) {
      //   //所有sheet的indicator名
      //   indicatorNames.push(val);
      // }
      var publicationInfList = [];
      for (let i = 0; i < this.tableUploadData.length; i++) {
        if (typeof this.tableUploadData[i]["刊物全称"] === "undefined") return;
        var publicationInf = {
          name: this.tableUploadData[i]["刊物全称"],
          abbr: this.tableUploadData[i]["刊物简称"],
          publisher: this.tableUploadData[i]["出版社"],
          url: this.tableUploadData[i]["网址"],
          level: this.tableUploadData[i]["收录级别"],
          year: this.importSelectYear,
          indicatorName: this.indicatorName,
        };
        publicationInfList.push(publicationInf);
      }
      that.postRequest("/publications", publicationInfList).then(
            (res) => {
             that.getTableByYear(that.indicatorID,that.year,that.indicatorType);
             that.getYearList();
            },
            () => {
              that.$message({
                type: "error",
                message: "添加失败!",
              });
            }
      );
    },
    async appendAward() {
      try {
        const postData = {
          name: this.awardInf.name,
          indicatorId: this.indicatorID,
          year: this.awardInf.year
        };

        await axios.post("/award/basic/awardType", postData).then((resp) => {
          if (resp.status === 200) {
            this.$message({
              type: "success",
              message: resp.msg,
            });

            this.getYearList(this.awardInf.year)
          }
        });

        await this.getTableByYear(this.indicatorID, postData.year, this.indicatorType, true);

        this.awardInf = {name:'',year: ''};
      } catch (error) {
        this.$message({
          type: "error",
          message: error.message || "追加奖项失败！"
        });
      }
    },
    appendAwardAsync() {
      var that = this;
      var AwardInfList = [];
      for (let i = 0; i < this.tableUploadData.length; i++) {
        if (typeof this.tableUploadData[i]["奖项名称"] === "undefined") return;
        var awardInfList = {
          name: this.tableUploadData[i]["奖项名称"],
          year: this.importSelectYear,
          indicatorId: this.indicatorID,
        };
        AwardInfList.push(awardInfList);
      }
      that.postRequest("/award/basic/awardType/import", AwardInfList).then(
            (res) => {
              that.getTableByYear(that.indicatorID,that.year,that.indicatorType);
              that.getYearList();
            },
            () => {
              that.$message({
                type: "error",
                message: "添加失败!",
              });
            }
      );
    },
    appendProjectAsync() {
      var that = this;
      var ProjectInfList = [];
      for (let i = 0; i < this.tableUploadData.length; i++) {
        if (typeof this.tableUploadData[i]["项目名称"] === "undefined") return;
        var projectInfList = {
          name: this.tableUploadData[i]["项目名称"],
          year: this.importSelectYear,
          indicatorId: this.indicatorID,
        };
        ProjectInfList.push(projectInfList);
      }
      that.postRequest("/projectType/import", ProjectInfList).then(
            (res) => {
              that.getTableByYear(that.indicatorID,that.year,that.indicatorType);
              that.getYearList();
            },
            () => {
              that.$message({
                type: "error",
                message: "添加失败!",
              });
            }
        );
    },
    appendProgram() {
      var postData = {
        name: this.programInf.name,
        indicatorId: this.indicatorID,
        year: this.programInf.year,
      };
      var that = this;
      axios
          .post("/projectType", postData)
          .then(function (resp) {
            const queryParams = new URLSearchParams({
              indicatorId: postData.indicatorId,
              year: postData.year,
              pageNum: that.currentPage,
              pageSize: that.PageSize,
            });
           that.$message({
            type: "success",
            message: resp.msg,
           });
            return axios.get("/projectByYear?" + queryParams.toString());
          })
          .then(function (resp) {
            that.tableData = resp.data;
            that.totalCount = resp.data.total;
            that.getYearList(postData.year)
            that.getTableByYear(that.indicatorID, postData.year, that.indicatorType, true);
          })
          .catch(function (error) {
            that.$message({
              type: "error",
              message: error.message || "请求失败!",
            });
          });
      this.programInf = {name:'',year:''};
    },
    appendDecision() {
      var postData = {
        name: this.decisionInf.name,
        indicatorId: this.indicatorID,
        year: this.decisionInf.year,
      };
      var that = this;
      axios.post("/decisionType", postData).then(function (resp) {
       if (resp){
        that.$message({
          type: "success",
          message: resp.msg,
        });
        that.getYearList(postData.year)
        that.getTableByYear(that.indicatorID, postData.year, that.indicatorType, true);
       }
      });
      this.decisionInf = {name: '',year:''};
    },
   appendCompetition() {
    var postData = {
     name: this.competitionInf.name,
     indicatorId: this.indicatorID,
     year: this.competitionInf.year,
    };
    var that = this;
    axios.post("/competitionType", postData).then(function (resp) {
     if (resp) {
      that.$message({
       type: "success",
       message: resp.msg,
      });
      that.getYearList(postData.year)
      that.getTableByYear(that.indicatorID, postData.year, that.indicatorType, true);
     }
    });
    this.competitionInf = {name: '', year: ''};
   },
    appendDecisionAsync() {
      var that = this;
      var DecisionInfList = [];
      for (let i = 0; i < this.tableUploadData.length; i++) {
        if (typeof this.tableUploadData[i]["成果名称"] === "undefined") return;
        var decisionInfList = {
          name: this.tableUploadData[i]["成果名称"],
          year: this.importSelectYear,
          indicatorId: this.indicatorID,
        };
        DecisionInfList.push(decisionInfList);
      }
      that.postRequest("/decision/basic/decisionType/import", DecisionInfList).then(
          (res) => {
            that.getTableByYear(that.indicatorID,that.year,that.indicatorType);
            that.getYearList();
          },
          () => {
            that.$message({
              type: "error",
              message: "添加失败!",
            });
          }
      );
    },
    getData(indicatorName, indicatorID, type) {
      //初始化
      this.currentPage = 1
      if (type != "授权专利" && type != "制定标准" && type != "学术专著和教材" && type != "制造或设计的产品") {
        this.isRoot = true;
        this.indicatorTypeZH = type;
        var that = this;
        const typeMapping = {
          学术论文: "publication",
          授权专利: "patent",
          科研获奖: "award",
          科研项目: "project",
          制定标准: "standard",
          决策咨询: "decision",
          学术专著和教材: "book",
          制造或设计的产品: "application",
          学科竞赛: "competition",
        };

        this.indicatorName = indicatorName;
        this.indicatorType = typeMapping[type];
        this.indicatorID = indicatorID;
        // this.years = [];
        // this.year = this.nowYear;
        // for (var i = 0; i < 5; i++) this.years.push(this.nowYear - i);
        this.getYearList().then(() => {
          this.getTableByYear(this.indicatorID, this.year, this.indicatorType);
        });
        // this.getTableByYear(indicatorID, this.year, this.indicatorType);
      } else {
        this.indicatorType = "";
        this.isRoot = false;
      }
    },
    closeSearch() {
      this.dialogVisibleSearch = false;
      this.pathVisible = false;
      this.publicationName = "";
      this.searchSelectType = "";
      this.searchPathInf.type = "";

      this.searchInf = "";
      // this.searchPathInf = ''
    },
    closeClone() {
      this.fromYear = "";
      this.toYear = new Date().getFullYear();
      // this.yearList = [];
      this.dialogVisibleClone = false;
    },
    searchUpdate(indicatorType) {
      this.rowData = this.searchInf2;
      var type = "";
      if (indicatorType === "学术论文") type = "publication";
      else if (indicatorType === "科研项目") type = "program";
      else if (indicatorType === "科研获奖奖") type = "award";
      else if (indicatorType === "决策咨询") type = "decision";
      this.update(type);
    },
    searchDelete(id, indicatorType) {
      var type = "";
      if (indicatorType === "学术论文") type = "publication";
      else if (indicatorType === "科研项目") type = "program";
      else if (indicatorType === "科研获奖奖") type = "award";
      else if (indicatorType === "决策咨询") type = "decision";
      this.remove(id, type);
    },
    importData() {
    },

    // 每页显示的条数
    handleSizeChange(val) {
      // 改变每页显示的条数
      this.PageSize = val;
      // 注意：在改变每页显示的条数时，要将页码显示到第一页
      this.currentPage = 1;
      this.getTableByYear(this.indicatorID, this.year, this.indicatorType);
    },
    // 显示第几页
    handleCurrentChange(val) {
      // 改变默认的页数
      this.currentPage = val;
      this.getTableByYear(this.indicatorID, this.year, this.indicatorType);
    },
    readFile(file) {
      //文件读取
      return new Promise((resolve) => {
        let reader = new FileReader();
        reader.readAsBinaryString(file); //以二进制的方式读取
        reader.onload = (ev) => {
          resolve(ev.target.result);
        };
      });
    },
    async handleAdd(ev) {
      //批量添加上传与前端解析
      let file = ev.raw;
      if (!file) {
        alert("上传失败！");
        return;
      } else {
        let data = await this.readFile(file);
        let workbook = XLSX.read(data, {type: "binary"}); //解析二进制格式数据
        var results = {};
        for (var i = 0; i < workbook.SheetNames.length; i++) {
          const firstSheetName = "sheet1";
          results[firstSheetName] = [];
          const worksheet = workbook.Sheets[firstSheetName];
          if (
              typeof worksheet.A1 != "undefined" &&
              (worksheet.A2.v != "《东华大学计算机学报》" ||
                  typeof worksheet.A3 != "undefined")
          ) {
            //判断一下有没有空表 或者空白表格
            results[firstSheetName] = [
              ...results[firstSheetName],
              ...XLSX.utils.sheet_to_json(worksheet),
            ];
          }
        }
        for (var i = 0; i < workbook.SheetNames.length; i++) {
          //比如有6种论文类别
          const firstSheetName = "sheet1";
          if (results[firstSheetName].length) {
            if (
                results[firstSheetName][0]["刊物全称"] == "《东华大学计算机学报》"
            )
              results[firstSheetName].shift();
          }
          for (var j = 0; j < results[firstSheetName].length; j++) {
            if (results[firstSheetName].length) {
              results[firstSheetName][j]["所属类别"] = firstSheetName;
              if (this.indicatorTypeZH == "学术论文" && typeof results[firstSheetName][j]["刊物全称"] == "undefined") {
                this.uploadResultError = true;
                this.errorMessage="刊物全称不可为空";
              }
              else if (this.indicatorTypeZH == "学术论文" && typeof results[firstSheetName][j]["出版社"] == "undefined") {
                this.uploadResultError = true;
                this.errorMessage="出版社不可为空";
              }
              else if (this.indicatorTypeZH == "科研项目" && typeof results[firstSheetName][j]["项目名称"] == "undefined"){
                this.uploadResultError = true;
                this.errorMessage="项目名称不可为空";
              }
              else if (this.indicatorTypeZH == "决策咨询" && typeof results[firstSheetName][j]["成果名称"] == "undefined"){
                this.uploadResultError = true;
                this.errorMessage="成果名称不可为空";
              }
              else if (this.indicatorTypeZH == "科研获奖" && typeof results[firstSheetName][j]["奖项名称"] == "undefined"){
                this.uploadResultError = true;
                this.errorMessage="奖项名称不可为空";
              }
            }
          }
          if (results[firstSheetName].length) {
            //该类别数据不为空
            this.tableUploadData = [
              ...this.tableUploadData,
              ...results[firstSheetName],
            ];
          }
        }
        if (!this.uploadResultError) {
          this.uploadResultValid = true;
        }
        this.uploadResult = true;
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
    handleClose() {
      //批量添加关闭
      this.uploadResult = false;
      this.fileList = [];
      this.tableUploadData = [];
      this.errorRow = [];
      this.uploadResultValid = false;
    },
    uploadConfirm() {
      //确认上传导入的文件
      var that = this;
      this.$confirm(
          "是否确定添加向" +
          this.importSelectYear +
          "年添加" +
          this.tableUploadData.length +
          "条记录" ,
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
      )
          .then(() => {
            //点击确认
              this.uploadAppend(this.indicatorTypeZH).then(() => {
                that.$message({
                  type: "success",
                  message: "添加成功",
                });
                that.uploadVisible = false;
                that.handleClose();
                // that.getTableByYear(that.indicatorID,that.year)
              });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消",
            });
          });
    },
    async uploadAppend(type) {
      switch (type){
        case "学术论文":await this.appendPublicationAsync();break;
        case "科研项目":await this.appendProjectAsync();break;
        case "科研获奖":await this.appendAwardAsync();break;
        case "决策咨询":await this.appendDecisionAsync();break;
      }
    },

    //导出excel模版按钮
    btnClickExport() {
      var sheetTitlesAndId = this.indicatorName; //保存所有sheet信息
      var tableSample = []; //sheet表头信息 标题
      this.importSelectType = this.indicatorType;
      if (this.importSelectType == "publication") {
        // this.typeOfAllPaper.forEach((item) => {
        //   sheetTitlesAndId.push(item);
        // });
        tableSample = this.tablePaperSample;
      } else if (this.importSelectType == "award") {
        // this.typeOfAllTechnical.forEach((item) => {
        //   sheetTitlesAndId.push(item);
        // });
        tableSample = this.tableTechnicalSample;
      } else if (this.importSelectType == "decision") {
        // this.typeOfAllDecision.forEach((item) => {
        //   sheetTitlesAndId.push(item);
        // });
        tableSample = this.tableDecisionSample;
      } else if (this.importSelectType == "project") {
        // this.typeOfAllProject.forEach((item) => {
        //   sheetTitlesAndId.push(item);
        // });
        tableSample = this.tableProjectSample;
      }
      // console.log(sheetTitlesAndId)
      var wb = XLSX.utils.book_new();
      var sheet = XLSX.utils.json_to_sheet(tableSample); //设置每个sheet的表头标题
      XLSX.utils.book_append_sheet(wb, sheet, 'sheet1');
      // console.log(wb);
      const workbookBlob = this.workbook2blob(wb);
      this.openDownloadDialog(workbookBlob, this.indicatorTypeZH+"模版.xlsx");
    },
    workbook2blob(workbook) {
      // 生成excel的配置项
      var wopts = {
        // 要生成的文件类型
        bookType: "xlsx",
        // 是否生成Shared String Table，官方解释是，如果开启生成速度会下降，但在低版本IOS设备上有更好的兼容性
        bookSST: false,
        type: "binary",
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
        type: "application/octet-stream",
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
        event.initMouseEvent(
            "click",
            true,
            false,
            window,
            0,
            0,
            0,
            0,
            0,
            false,
            false,
            false,
            false,
            0,
            null
        );
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
    checkUploadData({row, rowIndex}) {
      if (this.indicatorTypeZH == "学术论文" && (typeof row["刊物全称"] === "undefined" || typeof row["出版社"] === "undefined")) {
        return "warning-row";
      }
      if (this.indicatorTypeZH == "科研项目" && typeof row["项目名称"] === "undefined") {
        return "warning-row";
      }
      if (this.indicatorTypeZH == "决策咨询" && typeof row["成果名称"] === "undefined") {
        return "warning-row";
      }
      if (this.indicatorTypeZH == "科研获奖" && typeof row["奖项名称"] === "undefined") {
        return "warning-row";
      }
      return "";
    },
  },
};
</script>

<style>
.select_div_input {
  margin-left: 3px;
  width: 90%;
  height: 35px;
  position: relative;
  display: inline-block;
}

.select_div {
  border: 0.5px solid #fff;
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

.select_div_div:hover {
  color: blue;
}

.select_div_div {
  padding-bottom: 7px;
  padding-top: 7px;
  padding-left: 12px;
  width: 100%;
}

.el-form-item__label-wrap {
  margin-left: 0px !important;
}

.el-table.el-table__fixed-right {
  top: 0px;
  left: auto;
  right: 0px;
  border-left: 1px solid rgb(223, 230, 236);
  box-sizing: content-box;
}

.el-table .warning-row {
  background: oldlace;
}
</style>
