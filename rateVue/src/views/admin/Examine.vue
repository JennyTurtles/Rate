<template>
  <div>
    <div
            style="display: flex; justify-content: space-between; margin: 15px 0"
    >
      <div>
        <label>学生姓名：</label>
        <input type="text"
               style="margin-left:5px;width:80px;height:30px;padding:0 30px 0 15px;
                border:1px solid lightgrey;color:lightgrey;
                border-radius:4px;color:grey"
               placeholder="学生姓名"
               autocomplete="off"
               v-model="searchStudentName"
               id="select_stuname">
        <label style="margin-left:16px">名称：</label>
        <input type="text"
               style="margin-left:5px;width:80px;height:30px;padding:0 30px 0 15px;
                border:1px solid lightgrey;color:lightgrey;
                border-radius:4px;color:grey"
               placeholder="名称"
               v-model="searchPatentName"
               id="select_paperName">

<!--        <label style="margin-left:20px;">状态：</label>-->
<!--        <el-select-->
<!--                v-model="searchPatentState"-->
<!--                style="margin-left:3px;width:120px"-->
<!--                prefix-icon="el-icon-edit"-->
<!--                clearable-->
<!--                filterable-->
<!--                placeholder="状态筛选"-->
<!--        >-->
<!--          <el-option-->
<!--                  v-for="val in option"-->
<!--                  :key="val"-->
<!--                  :value="val"-->
<!--          >-->
<!--          </el-option>-->
<!--        </el-select>-->
<!--        <label style="margin-left:16px">积分范围：</label>-->
<!--        <el-select-->
<!--                v-model="pointFront"-->
<!--                style="margin-left:3px;width:60px"-->
<!--                prefix-icon="el-icon-edit"-->
<!--                clearable-->
<!--                filterable-->
<!--                placeholder="0"-->
<!--        >-->
<!--          <el-option-->
<!--                  style=""-->
<!--                  v-for="val in select_point"-->
<!--                  :key="val"-->
<!--                  :value="val"-->
<!--          >-->
<!--          </el-option>-->
<!--        </el-select>-->
<!--        <label >&nbsp; - &nbsp;</label>-->
<!--        <el-select-->
<!--                v-model="pointBack"-->
<!--                style="margin-left:3px;width:60px"-->
<!--                prefix-icon="el-icon-edit"-->
<!--                clearable-->
<!--                filterable-->
<!--                placeholder="12"-->
<!--        >-->
<!--          <el-option-->
<!--                  style=""-->
<!--                  v-for="val in select_point"-->
<!--                  :key="val"-->
<!--                  :value="val"-->
<!--          >-->
<!--          </el-option>-->
<!--        </el-select>-->
        <el-button
                icon="el-icon-search"
                type="primary"
                @click="searchPatentListByCondicitions(1, 10)"
                :disabled="showAdvanceSearchView"
                style="margin-left:30px"
        >
          搜索
        </el-button>
      </div>
        <el-button
                icon="el-icon-check"
                type="primary"
                @click="batchAudit"
                :disabled="selectedItems.length === 0"
                style="margin-left:30px"
        >
            批量审核
        </el-button>
    </div>




    <div style="margin-top: 10px">
        <el-table
                :data="patents"
                stripe
                border
                v-loading="loading"
                :header-cell-style="rowClass"
                element-loading-text="正在加载..."
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(0, 0, 0, 0.12)"
                style="width: 100%"
                @selection-change="handleSelectionChange"
        >
            <el-table-column
                    type="selection"
                    width="55"
            >
            </el-table-column>
        <el-table-column
                prop="studentName"
                align="center"
                label="申报人"
                min-width="15%"
        >
        </el-table-column>
          <el-table-column
                  prop="createtime"
                  align="center"
                  label="操作时间"
                  min-width="15%"
          >
          </el-table-column>
        <el-table-column
                prop="name"
                align="center"
                label="名称"
                min-width="15%"
        >
        </el-table-column>
        <el-table-column
              prop="type"
              label="类别"
              align="center"
              min-width="10%"
          >
        </el-table-column>
        <!-- width="200" -->
<!--        <el-table-column-->
<!--                prop="state"-->
<!--                label="状态"-->
<!--                min-width="10%"-->
<!--                align="center"-->
<!--        >-->
<!--          <template slot-scope="scope">-->
<!--            <span-->
<!--                    style="padding: 4px"-->
<!--                    size="mini"-->
<!--                    :model="emp.state"-->
<!--                    :style="(scope.row.state=='tea_reject' || scope.row.state=='adm_reject') ? {'color':'red'}:{'color':'gray'}"-->
<!--            >-->
<!--              {{scope.row.state=="commit"-->
<!--                ? "学生提交"-->
<!--                :scope.row.state=="tea_pass"-->
<!--                    ? "导师通过"-->
<!--                    :scope.row.state=="tea_reject"-->
<!--                        ? "导师驳回"-->
<!--                        :scope.row.state=="adm_pass"-->
<!--                            ? "管理员通过"-->
<!--                            :"管理员驳回"}}-->
<!--              </span>-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column
                prop="point"
                label="积分"
                align="center"
                min-width="8%"
        >
          <template slot-scope="scope">
            <span>{{scope.row.haveScore == 1 ? scope.row.point : 0}}</span>
            <span>/</span>
            <span>{{scope.row.point}}</span>
          </template>
        </el-table-column>
        <el-table-column
                min-width="15%"
                prop="operationList[0].remark"
                label="备注"
                align="center"
        >
        </el-table-column>
        <el-table-column
                min-width="20%"
                align="center"
                label="详情"
        >
          <template slot-scope="scope">
            <el-button
                    @click="showInfo(scope.row)"
                    style="padding: 4px"
                    size="mini"
            >审核</el-button
            >
            <el-button v-show="scope.row.state == 'adm_pass' ? true : false" @click="changePointMethod(scope.row)" style="padding: 4px"
                       size="mini">
              {{scope.row.changePointButton}}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--      <div style="display: flex; justify-content: flex-end; margin: 10px 0">-->
      <!--        <el-pagination-->
      <!--            background-->
      <!--            @current-change="currentChange"-->
      <!--            @size-change="sizeChange"-->
      <!--            :current-page="currentPage"-->
      <!--            layout="sizes, prev, pager, next, jumper, ->, total, slot"-->
      <!--            :total="totalCount"-->
      <!--            :page-sizes="pageSizes"-->
      <!--            :page-size="pageSize"-->
      <!--        >-->
      <!--        </el-pagination>-->
      <!--      </div>-->
    </div>




    <!-- 对话框 老师审核通过专利 -->
    <el-dialog :title="title"
               :visible.sync="dialogVisible_pass" width="30%" center>
      <!-- 确定审核通过该学生专利？ -->
      <el-form
              :label-position="labelPosition"
              label-width="80px"
              :model="emp"
              ref="empForm"
              style="margin-left: 60px"
      >
        <el-form-item label="专利ID:" prop="id">
          <span>{{ emp.id }}</span>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <!-- <el-button @click="dialogVisible_pass = false">取 消</el-button> -->
        <el-button type="primary" @click="auditing_commit('tea_pass')">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 对话框 老师驳回该学生专利 -->
    <el-dialog :title="title"
               :visible.sync="dialogVisible_reject" width="30%" center>

      <el-form
              :label-position="labelPosition"
              label-width="80px"
              :model="emp"
              ref="empForm"
              style="margin-left: 40px"
      >
        <el-form-item label="专利ID:" prop="id">
          <span>{{ emp.id }}</span>
        </el-form-item>
        <el-form-item label="驳回理由:">
          <el-input
                  type="textarea"
                  :rows="4"
                  v-model="reason"
                  placeholder="驳回理由"
          >
          </el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="auditing_commit('tea_reject')">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 学术论文查看详情按钮 -->
    <el-dialog
            class="showInfo_dialog"
            :title="title_show"
            :visible.sync="dialogVisible_showInfo_Paper"
            width="520px"
            center>
      <el-form
              :label-position="labelPosition"
              label-width="100px"
              :model="emp"
              ref="empForm"
              style="margin-left: 20px"
      >
        <el-form-item label="成果名称:">
            <span>{{ emp.name }}</span
            ><br/>
        </el-form-item>
        <el-form-item label="成果积分:">
            <span>{{ emp.point }}</span
            ><br/>
        </el-form-item>
        <el-form-item label="论文状态:">
            <span>{{
                emp.state == "commit"
                    ? "已提交"
                    : emp.state == "tea_pass"
                        ? "导师通过"
                        : emp.state == "tea_reject"
                            ? "导师驳回"
                            : emp.state == "adm_pass"
                                ? "管理员通过"
                                : "管理员驳回"
              }}</span
            ><br/>
        </el-form-item>
        <el-form-item label="作者人数:">
            <span>{{ emp.total }}</span
            ><br/>
        </el-form-item>
        <el-form-item label="作者列表:">
            <span>{{ emp.author }}</span
            ><br/>
        </el-form-item>
        <el-form-item label="作者排名:">
            <span>{{ emp.rank }}</span
            ><br/>
        </el-form-item>
        <el-form-item label="发表年月">
            <span>{{ emp.year }}-{{ emp.month }}</span
            ><br/>
        </el-form-item>

        <el-form-item label="证明材料:" prop="url">
          <span v-if="emp.url == '' || emp.url == null ? true : false">无证明材料</span>
          <div v-else>
            {{ emp.url | fileNameFilter }}
            <el-button @click="previewMethod('1')" v-show="isImage || isPdf">预览</el-button>
            <el-button @click="previewMethod('2')">下载</el-button>
          </div>
          <div style="margin-top: 5px">
            <el-image
                v-show="false"
                ref="previewImage"
                style="width: 100px; height: 100px"
                :src="previewUrl"
                :preview-src-list="previewImageSrcList">
            </el-image>
          </div>
        </el-form-item>
        <br/>
        <div>
          <span>历史操作:</span>
          <div
                  style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
            <div v-for="item in operList" :key="item.time"
                 style="margin-top:18px;color:gray;margin-left:14px">
              <div>
                {{ item.time | dataFormat }}&nbsp;&nbsp;&nbsp;&nbsp;{{
                item.operatorName
                }}&nbsp;&nbsp;&nbsp;&nbsp;{{ item.operationName }}
              </div>
              <div v-show="item.remark == '' ? false : true">驳回理由：{{ item.remark }}</div>
            </div>
          </div>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer" :model="emp">
            <el-button
                    id="but_pass"
                    v-show="(emp.state == 'commit' || (emp.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="(()=>{
                  if (role == 'teacher')
                   auditing_commit('tea_pass')
                  else if (role == 'admin')
                   auditing_commit('adm_pass')
                }) "
                    type="primary"
            >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(emp.state == 'commit' || (emp.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(emp.state=='tea_reject' || emp.state=='adm_reject' || emp.state == 'adm_pass' || (emp.state=='tea_pass' && role == 8))? true:false"
                    @click="dialogVisible_show = false"
                    type="primary"
            >关闭</el-button>
        </span>
    </el-dialog>
    <el-dialog v-model="emp" :visible.sync="isShowInfo">
      <el-input
              type="textarea"
              :rows="4"
              v-model="reason"
              placeholder="请输入驳回理由"
      >
      </el-input>
      <span slot="footer">
          <el-button @click="rejectDialogConfirm()" type="primary">确定</el-button>
          <el-button @click="isShowInfo = false">取消</el-button>
        </span>
    </el-dialog>
    <el-dialog :visible.sync="dialogPreviewPdfFile" style="width: 100%;height: 100%" fullscreen>
      <template v-if="isPdf">
        <vue-office-pdf
                :src="previewUrl"
                style="height: 100vh;"
        />
      </template>

    </el-dialog>

    <!-- 授权专利查看详情按钮 -->
    <el-dialog
            class="showInfo_dialog"
            :title="title_show"
            :visible.sync="dialogVisible_showInfo_Patent"
            width="520px"
            center
    >
      <el-form
              :label-position="labelPosition"
              label-width="80px"
              :model="currentPatent"
              style="margin-left: 20px">

        <el-form-item label="专利名称:" prop="name">
            <span>{{ currentPatent.name }}</span
            ><br/>
        </el-form-item>
        <el-form-item label="专利状态:" prop="grantedStatus">
            <span>{{ currentPatent.grantedStatus }}</span
            ><br/>
        </el-form-item>
        <el-form-item :label="currentPatent.grantedStatus + '年月:'" prop="date">
            <span>{{ currentPatent.date }}</span
            ><br/>
        </el-form-item>
        <el-form-item label="参与人:" prop="author">
            <span>{{ currentPatent.author }}</span
            ><br/>
        </el-form-item>
        <el-form-item label="参与人数:" prop="total">
            <span>{{ currentPatent.total }}</span
            ><br/>
        </el-form-item>
        <el-form-item label="作者排名:" prop="rank">
            <span>{{ currentPatent.rank }}</span
            ><br/>
        </el-form-item>
        <el-form-item label="成果积分:" prop="point">
            <span>{{ currentPatent.point }}</span
            ><br/>
        </el-form-item>
        <el-form-item label="成果状态:" prop="state">
            <span>{{
                currentPatent.state == "commit"
                    ? "已提交"
                    : currentPatent.state == "tea_pass"
                        ? "导师通过"
                        : currentPatent.state == "tea_reject"
                            ? "导师驳回"
                            : currentPatent.state == "adm_pass"
                                ? "管理员通过"
                                : "管理员驳回"
              }}</span
            ><br/>
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          <span v-if="currentPatent.url == '' || currentPatent.url == null ? true : false">无证明材料</span>
          <div v-else>
            {{ currentPatent.url | fileNameFilter }}
            <el-button @click="previewMethod('1')" v-show="isImage || isPdf">预览</el-button>
            <el-button @click="previewMethod('3')">下载</el-button>
          </div>
          <div style="margin-top: 5px">
            <el-image
                v-show="false"
                ref="previewImage"
                style="width: 100px; height: 100px"
                :src="previewUrl"
                :preview-src-list="previewImageSrcList">
            </el-image>
          </div>
        </el-form-item>
        <br/>
        <div>
          <span>历史操作:</span>
          <div
                  style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
            <div v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;margin-left:5px">
              <div>
                <p>
                  {{ item.time | dataFormat }}&nbsp;&nbsp;&nbsp;&nbsp;{{
                  item.operatorName
                  }}&nbsp;&nbsp;&nbsp;&nbsp;{{ item.operationName }}</p>
                <p v-show="item.remark == '' || item.remark == null ? false : true">驳回理由：{{ item.remark }}</p>
              </div>
            </div>
          </div>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer" :model="currentPatent">
            <el-button
                    id="but_pass"
                    v-show="(currentPatent.state == 'commit' || (currentPatent.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="(()=>{
                  if (role == 'teacher')
                   auditing_commit('tea_pass')
                  else if (role == 'admin')
                   auditing_commit('adm_pass')
                }) "
                    type="primary"
            >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentPatent.state == 'commit' || (currentPatent.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentPatent.state=='tea_reject' || currentPatent.state=='adm_reject' || currentPatent.state == 'adm_pass' || (currentPatent.state=='tea_pass' && role == 8))? true:false"
                    @click="dialogVisible_show = false"
                    type="primary"
            >关闭</el-button>
        </span>
    </el-dialog>
    <el-dialog v-model="currentPatent" :visible.sync="isShowInfo">
      <el-input
              type="textarea"
              :rows="4"
              v-model="reason"
              placeholder="请输入驳回理由"
      >
      </el-input>
      <span slot="footer">
          <el-button @click="rejectDialogConfirm()" type="primary">确定</el-button>
          <el-button @click="isShowInfo = false">取消</el-button>
        </span>
    </el-dialog>
    <el-dialog :visible.sync="dialogPreviewPdfFile" style="width: 100%;height: 100%" fullscreen>
      <template v-if="isPdf">
        <vue-office-pdf
                :src="previewUrl"
                style="height: 100vh;"
        />
      </template>

    </el-dialog>

    <!-- 科研获奖查看详情-->
    <el-dialog
            class="showInfo_dialog"
            :title="title_show"
            :visible.sync="dialogVisible_showInfo_ResearchAward"
            width="520px"
            center
    >
      <el-form
              :label-position="labelPosition"
              label-width="80px"
              :model="currentAward"
              style="margin-left: 20px">
        <el-form-item label="奖励名称:">
            <span>{{ currentAward.name }}</span
            ><br/>
        </el-form-item>
        <el-form-item label="获奖人:">
            <span>{{ currentAward.author }}</span
            >
        </el-form-item>
        <el-form-item label="奖励类别:">
            <span>{{ currentAward.awardType.name }}</span
            >
        </el-form-item>
        <el-form-item label="奖励级别:">
            <span>{{ currentAward.awardLevel }}</span
            >
        </el-form-item>
        <el-form-item label="奖励积分:">
            <span>{{ currentAward.point }}</span
            >
        </el-form-item>
        <el-form-item label="作者人数:">
            <span>{{ currentAward.total }}</span
            >
        </el-form-item>
        <el-form-item label="作者排名:">
            <span>{{ currentAward.rank }}</span
            >
        </el-form-item>
        <el-form-item label="获奖年月:">
            <span>{{ currentAward.date }}</span
            >
        </el-form-item>
        <el-form-item label="成果状态:">
            <span>{{
                currentAward.state == "commit"
                    ? "已提交"
                    : currentAward.state == "tea_pass"
                        ? "导师通过"
                        : currentAward.state == "tea_reject"
                            ? "导师驳回"
                            : currentAward.state == "adm_pass"
                                ? "管理员通过"
                                : "管理员驳回"
              }}</span
            ><br/>
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          <span v-if="currentAward.url == '' || currentAward.url == null ? true : false">无证明材料</span>
          <div v-else>
            {{ currentAward.url | fileNameFilter }}
            <el-button @click="previewMethod('1')" v-show="isImage || isPdf">预览</el-button>
            <el-button @click="previewMethod('4')">下载</el-button>
          </div>
          <div style="margin-top: 5px">
            <el-image
                v-show="false"
                ref="previewImage"
                style="width: 100px; height: 100px"
                :src="previewUrl"
                :preview-src-list="previewImageSrcList">
            </el-image>
          </div>
        </el-form-item>
        <br/>
        <div>
          <span>历史操作:</span>
          <div
                  style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
            <div v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;margin-left:5px">
              <div>
                <p>
                  {{ item.time | dataFormat }}&nbsp;&nbsp;&nbsp;&nbsp;{{
                  item.operatorName
                  }}&nbsp;&nbsp;&nbsp;&nbsp;{{ item.operationName }}</p>
                <p v-show="item.remark == '' || item.remark == null ? false : true">驳回理由：{{ item.remark }}</p>
              </div>
            </div>
          </div>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer" :model="emp">
            <el-button
                    id="but_pass"
                    v-show="(currentAward.state == 'commit' || (currentAward.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="(()=>{
                  if (role == 'teacher')
                   auditing_commit('tea_pass')
                  else if (role == 'admin')
                   auditing_commit('adm_pass')
                }) "
                    type="primary"
            >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentAward.state == 'commit' || (currentAward.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentAward.state=='tea_reject' || currentAward.state=='adm_reject' || currentAward.state == 'adm_pass' || (currentAward.state=='tea_pass' && role == 8))? true:false"
                    @click="dialogVisible_show = false"
                    type="primary"
            >关闭</el-button>
        </span>
    </el-dialog>
    <el-dialog v-model="currentAward" :visible.sync="isShowInfo">
      <el-input
              type="textarea"
              :rows="4"
              v-model="reason"
              placeholder="请输入驳回理由"
      >
      </el-input>
      <span slot="footer">
          <el-button @click="rejectDialogConfirm()" type="primary">确定</el-button>
          <el-button @click="isShowInfo = false">取消</el-button>
        </span>
    </el-dialog>
    <el-dialog :visible.sync="dialogPreviewPdfFile" style="width: 100%;height: 100%" fullscreen>
      <template v-if="isPdf">
        <vue-office-pdf
                :src="previewUrl"
                style="height: 100vh;"
        />
      </template>

    </el-dialog>

    <!--学术专著和教材查看详情-->
    <el-dialog
            class="showInfo_dialog"
            :title="title_show"
            :visible.sync="dialogVisible_showInfo_AcademicMonograph"
            width="520px"
            center
    >
      <el-form
              :label-position="labelPosition"
              label-width="80px"
              :model="currentMonograph"
              style="margin-left: 20px">
        <el-form-item label="著作名称:">
            <span>{{ currentMonograph.name }}</span
            ><br/>
        </el-form-item>
        <el-form-item label="作者列表:">
            <span>{{ currentMonograph.author }}</span
            >
        </el-form-item>
        <el-form-item label="作者人数:">
            <span>{{ currentMonograph.total }}</span
            >
        </el-form-item>
        <el-form-item label="作者排名:">
            <span>{{ currentMonograph.rank }}</span
            >
        </el-form-item>
        <el-form-item label="完成年份:">
            <span>{{ currentMonograph.date }}</span
            >
        </el-form-item>
        <el-form-item label="出版社:">
            <span>{{ currentMonograph.publisher }}</span
            >
        </el-form-item>
        <el-form-item label="ISBN:">
            <span>{{ currentMonograph.isbn }}</span
            >
        </el-form-item>
        <el-form-item label="成果积分:">
            <span>{{ currentMonograph.point }}</span
            >
        </el-form-item>
        <el-form-item label="证明材料:" prop="url">
          <span v-if="currentMonograph.url == '' || currentMonograph.url == null ? true : false">无证明材料</span>
          <div v-else>
            {{ currentMonograph.url | fileNameFilter }}
            <el-button @click="previewMethod('1')" v-show="isImage || isPdf">预览</el-button>
            <el-button @click="previewMethod('5')">下载</el-button>
          </div>
          <div style="margin-top: 5px">
            <el-image
                v-show="false"
                ref="previewImage"
                style="width: 100px; height: 100px"
                :src="previewUrl"
                :preview-src-list="previewImageSrcList">
            </el-image>
          </div>
        </el-form-item>
        <br/>
        <div>
          <span>历史操作:</span>
          <div
                  style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
            <div v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;margin-left:5px">
              <div>
                <p>
                  {{ item.time | dataFormat }}&nbsp;&nbsp;&nbsp;&nbsp;{{
                  item.operatorName
                  }}&nbsp;&nbsp;&nbsp;&nbsp;{{ item.operationName }}</p>
                <p v-show="item.remark == '' || item.remark == null ? false : true">驳回理由：{{ item.remark }}</p>
              </div>
            </div>
          </div>
        </div>
      </el-form>
      <span slot="footer" class="dialog-footer" :model="emp">
            <el-button
                    id="but_pass"
                    v-show="(currentMonograph.state == 'commit' || (currentMonograph.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="(()=>{
                  if (role == 'teacher')
                   auditing_commit('tea_pass')
                  else if (role == 'admin')
                   auditing_commit('adm_pass')
                }) "
                    type="primary"
            >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentMonograph.state == 'commit' || (currentMonograph.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentMonograph.state=='tea_reject' || currentMonograph.state=='adm_reject' || currentMonograph.state == 'adm_pass' || (currentMonograph.state=='tea_pass' && role == 8))? true:false"
                    @click="dialogVisible_show = false"
                    type="primary"
            >关闭</el-button>
        </span>
    </el-dialog>
    <el-dialog v-model="currentMonograph" :visible.sync="isShowInfo">
      <el-input
              type="textarea"
              :rows="4"
              v-model="reason"
              placeholder="请输入驳回理由"
      >
      </el-input>
      <span slot="footer">
          <el-button @click="rejectDialogConfirm()" type="primary">确定</el-button>
          <el-button @click="isShowInfo = false">取消</el-button>
        </span>
    </el-dialog>
    <el-dialog :visible.sync="dialogPreviewPdfFile" style="width: 100%;height: 100%" fullscreen>
      <template v-if="isPdf">
        <vue-office-pdf
                :src="previewUrl"
                style="height: 100vh;"
        />
      </template>

    </el-dialog>

      <!--纵向科研项目查看详情-->
      <el-dialog
              class="showInfo_dialog"
              :title="title_show"
              :visible.sync="dialogVisible_showInfo_ResearchProject"
              width="520px"
              center
      >
          <el-form
                  :label-position="labelPosition"
                  label-width="80px"
                  :model="currentProject"
                  style="margin-left: 20px">
              <el-form-item label="项目名称:">
            <span>{{ currentProject.name }}</span
            ><br/>
              </el-form-item>
              <el-form-item label="作者列表:">
            <span>{{ currentProject.author }}</span
            >
              </el-form-item>
              <el-form-item label="立项年月:">
            <span>{{ currentProject.startDate }}</span
            >
              </el-form-item>
              <el-form-item label="结项年月:">
            <span>{{ currentProject.endDate }}</span
            >
              </el-form-item>
              <el-form-item label="项目类别:">
            <span>{{ currentProject.projectType.name }}</span
            >
              </el-form-item>
              <el-form-item label="作者人数:">
            <span>{{ currentProject.total }}</span
            >
              </el-form-item>
              <el-form-item label="作者排名:">
            <span>{{ currentProject.rank }}</span
            >
              </el-form-item>
            <el-form-item label="证明材料:" prop="url">
              <span v-if="currentProject.url == '' || currentProject.url == null ? true : false">无证明材料</span>
              <div v-else>
                {{ currentProject.url | fileNameFilter }}
                <el-button @click="previewMethod('1')" v-show="isImage || isPdf">预览</el-button>
                <el-button @click="previewMethod('6')">下载</el-button>
              </div>
              <div style="margin-top: 5px">
                <el-image
                    v-show="false"
                    ref="previewImage"
                    style="width: 100px; height: 100px"
                    :src="previewUrl"
                    :preview-src-list="previewImageSrcList">
                </el-image>
              </div>
            </el-form-item>
              <br/>
              <div>
                  <span>历史操作:</span>
                  <div
                          style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
                      <div v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;margin-left:5px">
                          <div>
                              <p>
                                  {{ item.time | dataFormat }}&nbsp;&nbsp;&nbsp;&nbsp;{{
                                  item.operatorName
                                  }}&nbsp;&nbsp;&nbsp;&nbsp;{{ item.operationName }}</p>
                              <p v-show="item.remark == '' || item.remark == null ? false : true">驳回理由：{{ item.remark }}</p>
                          </div>
                      </div>
                  </div>
              </div>
          </el-form>

          <span slot="footer" class="dialog-footer" :model="currentProject">
            <el-button
                    id="but_pass"
                    v-show="(currentProject.state == 'commit' || (currentProject.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="(()=>{
                  if (role == 'teacher')
                   auditing_commit('tea_pass')
                  else if (role == 'admin')
                   auditing_commit('adm_pass')
                }) "
                    type="primary"
            >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentProject.state == 'commit' || (currentProject.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentProject.state=='tea_reject' || currentProject.state=='adm_reject' || currentProject.state == 'adm_pass' || (currentProject.state=='tea_pass' && role == 8))? true:false"
                    @click="dialogVisible_show = false"
                    type="primary"
            >关闭</el-button>
        </span>
      </el-dialog>
      <el-dialog v-model="currentProject" :visible.sync="isShowInfo">
          <el-input
                  type="textarea"
                  :rows="4"
                  v-model="reason"
                  placeholder="请输入驳回理由"
          >
          </el-input>
          <span slot="footer">
          <el-button @click="rejectDialogConfirm()" type="primary">确定</el-button>
          <el-button @click="isShowInfo = false">取消</el-button>
        </span>
      </el-dialog>
      <el-dialog :visible.sync="dialogPreviewPdfFile" style="width: 100%;height: 100%" fullscreen>
          <tcurrentProjectlate v-if="isPdf">
              <vue-office-pdf
                      :src="previewUrl"
                      style="height: 100vh;"
              />
          </tcurrentProjectlate>

      </el-dialog> <!--纵向科研项目查看详情-->
      <el-dialog
              class="showInfo_dialog"
              :title="title_show"
              :visible.sync="dialogVisible_showInfo_ResearchProject"
              width="520px"
              center
      >
          <el-form
                  :label-position="labelPosition"
                  label-width="80px"
                  :model="currentProject"
                  style="margin-left: 20px">
              <el-form-item label="项目名称:">
            <span>{{ currentProject.name }}</span
            ><br/>
              </el-form-item>
              <el-form-item label="作者列表:">
            <span>{{ currentProject.author }}</span
            >
              </el-form-item>
              <el-form-item label="立项年月:">
            <span>{{ currentProject.startDate }}</span
            >
              </el-form-item>
              <el-form-item label="结项年月:">
            <span>{{ currentProject.endDate }}</span
            >
              </el-form-item>
              <el-form-item label="项目类别:">
            <span>{{ currentProject.projectType.name }}</span
            >
              </el-form-item>
              <el-form-item label="作者人数:">
            <span>{{ currentProject.total }}</span
            >
              </el-form-item>
              <el-form-item label="作者排名:">
            <span>{{ currentProject.rank }}</span
            >
              </el-form-item>
            <el-form-item label="证明材料:" prop="url">
              <span v-if="currentProject.url == '' || currentProject.url == null ? true : false">无证明材料</span>
              <div v-else>
                {{ currentProject.url | fileNameFilter }}
                <el-button @click="previewMethod('1')" v-show="isImage || isPdf">预览</el-button>
                <el-button @click="previewMethod('6')">下载</el-button>
              </div>
              <div style="margin-top: 5px">
                <el-image
                    v-show="false"
                    ref="previewImage"
                    style="width: 100px; height: 100px"
                    :src="previewUrl"
                    :preview-src-list="previewImageSrcList">
                </el-image>
              </div>
            </el-form-item>
              <br/>
              <div>
                  <span>历史操作:</span>
                  <div
                          style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
                      <div v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;margin-left:5px">
                          <div>
                              <p>
                                  {{ item.time | dataFormat }}&nbsp;&nbsp;&nbsp;&nbsp;{{
                                  item.operatorName
                                  }}&nbsp;&nbsp;&nbsp;&nbsp;{{ item.operationName }}</p>
                              <p v-show="item.remark == '' || item.remark == null ? false : true">驳回理由：{{ item.remark }}</p>
                          </div>
                      </div>
                  </div>
              </div>
          </el-form>

          <span slot="footer" class="dialog-footer" :model="currentProject">
            <el-button
                    id="but_pass"
                    v-show="(currentProject.state == 'commit' || (currentProject.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="(()=>{
                  if (role == 'teacher')
                   auditing_commit('tea_pass')
                  else if (role == 'admin')
                   auditing_commit('adm_pass')
                }) "
                    type="primary"
            >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentProject.state == 'commit' || (currentProject.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentProject.state=='tea_reject' || currentProject.state=='adm_reject' || currentProject.state == 'adm_pass' || (currentProject.state=='tea_pass' && role == 8))? true:false"
                    @click="dialogVisible_show = false"
                    type="primary"
            >关闭</el-button>
        </span>
      </el-dialog>
      <el-dialog v-model="currentProject" :visible.sync="isShowInfo">
          <el-input
                  type="textarea"
                  :rows="4"
                  v-model="reason"
                  placeholder="请输入驳回理由"
          >
          </el-input>
          <span slot="footer">
          <el-button @click="rejectDialogConfirm()" type="primary">确定</el-button>
          <el-button @click="isShowInfo = false">取消</el-button>
        </span>
      </el-dialog>
      <el-dialog :visible.sync="dialogPreviewPdfFile" style="width: 100%;height: 100%" fullscreen>
          <tcurrentProjectlate v-if="isPdf">
              <vue-office-pdf
                      :src="previewUrl"
                      style="height: 100vh;"
              />
          </tcurrentProjectlate>

      </el-dialog>

      <!--项目开发查看详情-->
      <el-dialog
              class="showInfo_dialog"
              :title="title_show"
              :visible.sync="dialogVisible_showInfo_HorizontalResearchProject"
              width="520px"
              center
      >
          <el-form
                  :label-position="labelPosition"
                  label-width="80px"
                  :model="currentProgram"
                  style="margin-left: 20px">
            <el-form-item label="成果名称:">
                <span>项目开发申报</span
                ><br/>
            </el-form-item>
            <el-form-item label="项目时长:" >
                <span>{{ currentProgram.workHours }}</span
                ><br/>
            </el-form-item>
            <el-form-item label="成果积分:" prop="point">
                <span>{{ currentProgram.point }}</span
                ><br/>
            </el-form-item>
            <el-form-item label="成果状态:" prop="state">
              <span>{{
                currentProgram.state == "commit"
                    ? "已提交"
                    : currentProgram.state == "tea_pass"
                        ? "导师通过"
                        : currentProgram.state == "tea_reject"
                            ? "导师驳回"
                            : currentProgram.state == "adm_pass"
                                ? "管理员通过"
                                : "管理员驳回"
              }}</span
            ><br/>
              </el-form-item>
              <br/>
              <div>
                  <span>历史操作:</span>
                  <div
                          style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
                      <div v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;margin-left:5px">
                          <div>
                              <p>
                                  {{ item.time | dataFormat }}&nbsp;&nbsp;&nbsp;&nbsp;{{
                                  item.operatorName
                                  }}&nbsp;&nbsp;&nbsp;&nbsp;{{ item.operationName }}</p>
                              <p v-show="item.remark == '' || item.remark == null ? false : true">驳回理由：{{ item.remark }}</p>
                          </div>
                      </div>
                  </div>
              </div>
          </el-form>

          <span slot="footer" class="dialog-footer" :model="currentProgram">
            <el-button
                    id="but_pass"
                    v-show="(currentProgram.state == 'commit' || (currentProgram.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="(()=>{
                  if (role == 'teacher')
                   auditing_commit('tea_pass')
                  else if (role == 'admin')
                   auditing_commit('adm_pass')
                }) "
                    type="primary"
            >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentProgram.state == 'commit' || (currentProgram.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentProgram.state=='tea_reject' || currentProgram.state=='adm_reject' || currentProgram.state == 'adm_pass' || (currentProgram.state=='tea_pass' && role == 8))? true:false"
                    @click="dialogVisible_show = false"
                    type="primary"
            >关闭</el-button>
        </span>
      </el-dialog>
      <el-dialog v-model="currentProject" :visible.sync="isShowInfo">
          <el-input
                  type="textarea"
                  :rows="4"
                  v-model="reason"
                  placeholder="请输入驳回理由"
          >
          </el-input>
          <span slot="footer">
          <el-button @click="rejectDialogConfirm()" type="primary">确定</el-button>
          <el-button @click="isShowInfo = false">取消</el-button>
        </span>
      </el-dialog>
      <el-dialog :visible.sync="dialogPreviewPdfFile" style="width: 100%;height: 100%" fullscreen>
          <tcurrentProjectlate v-if="isPdf">
              <vue-office-pdf
                      :src="previewUrl"
                      style="height: 100vh;"
              />
          </tcurrentProjectlate>

      </el-dialog>

      <!--学科竞赛查看详情-->
      <el-dialog
              class="showInfo_dialog"
              :title="title_show"
              :visible.sync="dialogVisible_showInfo_AcademicCompetition"
              width="520px"
              center
      >
          <el-form
                  :label-position="labelPosition"
                  label-width="80px"
                  :model="currentCompetition"
                  style="margin-left: 20px">
              <el-form-item label="竞赛名称:">
            <span>{{ currentCompetition.name }}</span
            ><br/>
              </el-form-item>
              <el-form-item label="获奖人:">
            <span>{{ currentCompetition.author }}</span
            >
              </el-form-item>
              <el-form-item label="获奖类别:">
            <span>{{ currentCompetition.competitionType.name }}</span
            >
              </el-form-item>
              <el-form-item label="获奖年月:">
            <span>{{ currentCompetition.date }}</span
            >
              </el-form-item>
              <el-form-item label="获奖人数:">
            <span>{{ currentCompetition.total }}</span
            >
              </el-form-item>
              <el-form-item label="获奖排名:">
            <span>{{ currentCompetition.rank }}</span
            >
              </el-form-item>
              <el-form-item label="成果状态:">
            <span>{{
                currentCompetition.state == "commit"
                    ? "已提交"
                    : currentCompetition.state == "tea_pass"
                        ? "导师通过"
                        : currentCompetition.state == "tea_reject"
                            ? "导师驳回"
                            : currentCompetition.state == "adm_pass"
                                ? "管理员通过"
                                : "管理员驳回"
              }}</span
            ><br/>
              </el-form-item>
            <el-form-item label="证明材料:" prop="url">
              <span v-if="currentCompetition.url == '' || currentCompetition.url == null ? true : false">无证明材料</span>
              <div v-else>
                {{ currentProject.url | fileNameFilter }}
                <el-button @click="previewMethod('1')" v-show="isImage || isPdf">预览</el-button>
                <el-button @click="previewMethod('7')">下载</el-button>
              </div>
              <div style="margin-top: 5px">
                <el-image
                    v-show="false"
                    ref="previewImage"
                    style="width: 100px; height: 100px"
                    :src="previewUrl"
                    :preview-src-list="previewImageSrcList">
                </el-image>
              </div>
            </el-form-item>
              <br/>
              <div>
                  <span>历史操作:</span>
                  <div
                          style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
                      <div v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;margin-left:5px">
                          <div>
                              <p>
                                  {{ item.time | dataFormat }}&nbsp;&nbsp;&nbsp;&nbsp;{{
                                  item.operatorName
                                  }}&nbsp;&nbsp;&nbsp;&nbsp;{{ item.operationName }}</p>
                              <p v-show="item.remark == '' || item.remark == null ? false : true">驳回理由：{{ item.remark }}</p>
                          </div>
                      </div>
                  </div>
              </div>
          </el-form>

          <span slot="footer" class="dialog-footer" :model="currentCompetition">
            <el-button
                    id="but_pass"
                    v-show="(currentCompetition.state == 'commit' || (currentCompetition.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="(()=>{
                  if (role == 'teacher')
                   auditing_commit('tea_pass')
                  else if (role == 'admin')
                   auditing_commit('adm_pass')
                }) "
                    type="primary"
            >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentCompetition.state == 'commit' || (currentCompetition.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentCompetition.state=='tea_reject' || currentCompetition.state=='adm_reject' || currentCompetition.state == 'adm_pass' || (currentCompetition.state=='tea_pass' && role == 8))? true:false"
                    @click="dialogVisible_show = false"
                    type="primary"
            >关闭</el-button>
        </span>
      </el-dialog>
      <el-dialog v-model="currentCompetition" :visible.sync="isShowInfo">
          <el-input
                  type="textarea"
                  :rows="4"
                  v-model="reason"
                  placeholder="请输入驳回理由"
          >
          </el-input>
          <span slot="footer">
          <el-button @click="rejectDialogConfirm()" type="primary">确定</el-button>
          <el-button @click="isShowInfo = false">取消</el-button>
        </span>
      </el-dialog>
      <el-dialog :visible.sync="dialogPreviewPdfFile" style="width: 100%;height: 100%" fullscreen>
          <tcurrentCompetitionlate v-if="isPdf">
              <vue-office-pdf
                      :src="previewUrl"
                      style="height: 100vh;"
              />
          </tcurrentCompetitionlate>

      </el-dialog>

      <!--决策咨询查看详情-->
      <el-dialog
              class="showInfo_dialog"
              :title="title_show"
              :visible.sync="dialogVisible_showInfo_Decision"
              width="520px"
              center
      >
          <el-form
                  :label-position="labelPosition"
                  label-width="80px"
                  :model="currentDecision"
                  style="margin-left: 20px">
              <el-form-item label="决策名称:">
            <span>{{ currentDecision.name }}</span
            ><br />
              </el-form-item>
              <el-form-item label="制定人:">
            <span>{{ currentDecision.author }}</span
            >
              </el-form-item>
              <el-form-item label="制定年月:">
            <span>{{ currentDecision.date }}</span
            >
              </el-form-item>
              <el-form-item label="制定人数:">
            <span>{{ currentDecision.total }}</span
            >
              </el-form-item>
              <el-form-item label="作者排名:">
            <span>{{ currentDecision.rank }}</span
            >
              </el-form-item>
              <el-form-item label="成果状态:">
            <span>{{currentDecision.state=="commit"
                ? "已提交"
                :currentDecision.state=="tea_pass"
                    ? "导师通过"
                    :currentDecision.state=="tea_reject"
                        ? "导师驳回"
                        :currentDecision.state=="adm_pass"
                            ? "管理员通过"
                            :"管理员驳回"}}</span
            ><br />
              </el-form-item>
              <el-form-item label="备  注:">
                  <span>{{ currentDecision.remark }}</span>
              </el-form-item>
            <el-form-item label="证明材料:" prop="url">
              <span v-if="currentDecision.url == '' || currentDecision.url == null ? true : false">无证明材料</span>
              <div v-else>
                {{ currentDecision.url | fileNameFilter }}
                <el-button @click="previewMethod('1')" v-show="isImage || isPdf">预览</el-button>
                <el-button @click="previewMethod('8')">下载</el-button>
              </div>
              <div style="margin-top: 5px">
                <el-image
                    v-show="false"
                    ref="previewImage"
                    style="width: 100px; height: 100px"
                    :src="previewUrl"
                    :preview-src-list="previewImageSrcList">
                </el-image>
              </div>
            </el-form-item>
              <br />
              <div >
                  <span>历史操作:</span>
                  <div style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
                      <div  v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;margin-left:5px">
                          <div >
                              <p>{{item.time | dataFormat}}&nbsp;&nbsp;&nbsp;&nbsp;{{item.operatorName}}&nbsp;&nbsp;&nbsp;&nbsp;{{item.operationName}}</p>
                              <p v-show="item.remark == '' || item.remark == null ? false : true">驳回理由：{{item.remark}}</p>
                          </div>
                      </div>
                  </div>
              </div>
          </el-form>

          <span slot="footer" class="dialog-footer" :model="currentDecision">
            <el-button
                    id="but_pass"
                    v-show="(currentDecision.state == 'commit' || (currentDecision.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="(()=>{
                  if (role == 'teacher')
                   auditing_commit('tea_pass')
                  else if (role == 'admin')
                   auditing_commit('adm_pass')
                }) "
                    type="primary"
            >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentDecision.state == 'commit' || (currentDecision.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentDecision.state=='tea_reject' || currentDecision.state=='adm_reject' || currentDecision.state == 'adm_pass' || (currentDecision.state=='tea_pass' && role == 8))? true:false"
                    @click="dialogVisible_show = false"
                    type="primary"
            >关闭</el-button>
        </span>
      </el-dialog>
      <el-dialog v-model="currentDecision" :visible.sync="isShowInfo">
          <el-input
                  type="textarea"
                  :rows="4"
                  v-model="reason"
                  placeholder="请输入驳回理由"
          >
          </el-input>
          <span slot="footer">
          <el-button @click="rejectDialogConfirm()" type="primary">确定</el-button>
          <el-button @click="isShowInfo = false">取消</el-button>
        </span>
      </el-dialog>
      <el-dialog :visible.sync="dialogPreviewPdfFile" style="width: 100%;height: 100%" fullscreen>
          <tcurrentDecisionlate v-if="isPdf">
              <vue-office-pdf
                      :src="previewUrl"
                      style="height: 100vh;"
              />
          </tcurrentDecisionlate>

      </el-dialog>

      <!--项目文档查看详情-->
      <el-dialog
              class="showInfo_dialog"
              :title="title_show"
              :visible.sync="dialogVisible_showInfo_Product"
              width="520px"
              center
      >
          <el-form
                  :label-position="labelPosition"
                  label-width="80px"
                  :model="currentProduct"
                  style="margin-left: 20px">

              <el-form-item label="文档名称:" prop="name">
            <span>{{ currentProduct.name }}</span
            ><br/>
              </el-form-item>
              <el-form-item label="完成日期:" prop="date">
            <span>{{ currentProduct.date }}</span
            ><br/>
              </el-form-item>
              <el-form-item label="作者列表:" prop="author">
            <span>{{ currentProduct.author }}</span
            ><br/>
              </el-form-item>
              <el-form-item label="作者人数:" prop="total">
            <span>{{ currentProduct.total }}</span
            ><br/>
              </el-form-item>
              <el-form-item label="作者排名:" prop="rank">
            <span>{{ currentProduct.rank }}</span
            ><br/>
              </el-form-item>
              <el-form-item label="成果积分:" prop="point">
            <span>{{ currentProduct.point }}</span
            ><br/>
              </el-form-item>
              <el-form-item label="成果状态:" prop="state">
            <span>{{
                currentProduct.state == "commit"
                    ? "已提交"
                    : currentProduct.state == "tea_pass"
                        ? "导师通过"
                        : currentProduct.state == "tea_reject"
                            ? "导师驳回"
                            : currentProduct.state == "adm_pass"
                                ? "管理员通过"
                                : "管理员驳回"
              }}</span
            ><br/>
              </el-form-item>
            <el-form-item label="证明材料:" prop="url">
              <span v-if="currentProduct.url == '' || currentProduct.url == null ? true : false">无证明材料</span>
              <div v-else>
                {{ currentProduct.url | fileNameFilter }}
                <el-button @click="previewMethod('1')" v-show="isImage || isPdf">预览</el-button>
                <el-button @click="previewMethod('9')">下载</el-button>
              </div>
              <div style="margin-top: 5px">
                <el-image
                    v-show="false"
                    ref="previewImage"
                    style="width: 100px; height: 100px"
                    :src="previewUrl"
                    :preview-src-list="previewImageSrcList">
                </el-image>
              </div>
            </el-form-item>
              <br/>
              <div>
                  <span>历史操作:</span>
                  <div
                          style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
                      <div v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;margin-left:5px">
                          <div>
                              <p>
                                  {{ item.time | dataFormat }}&nbsp;&nbsp;&nbsp;&nbsp;{{
                                  item.operatorName
                                  }}&nbsp;&nbsp;&nbsp;&nbsp;{{ item.operationName }}</p>
                              <p v-show="item.remark == '' || item.remark == null ? false : true">驳回理由：{{ item.remark }}</p>
                          </div>
                      </div>
                  </div>
              </div>
          </el-form>

          <span slot="footer" class="dialog-footer" :model="currentProduct">
            <el-button
                    id="but_pass"
                    v-show="(currentProduct.state == 'commit' || (currentProduct.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="(()=>{
                  if (role == 'teacher')
                   auditing_commit('tea_pass')
                  else if (role == 'admin')
                   auditing_commit('adm_pass')
                }) "
                    type="primary"
            >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentProduct.state == 'commit' || (currentProduct.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentProduct.state=='tea_reject' || currentProduct.state=='adm_reject' || currentProduct.state == 'adm_pass' || (currentProduct.state=='tea_pass' && role == 8))? true:false"
                    @click="dialogVisible_show = false"
                    type="primary"
            >关闭</el-button>
        </span>
      </el-dialog>
      <el-dialog v-model="currentProduct" :visible.sync="isShowInfo">
          <el-input
                  type="textarea"
                  :rows="4"
                  v-model="reason"
                  placeholder="请输入驳回理由"
          >
          </el-input>
          <span slot="footer">
          <el-button @click="rejectDialogConfirm()" type="primary">确定</el-button>
          <el-button @click="isShowInfo = false">取消</el-button>
        </span>
      </el-dialog>
      <el-dialog :visible.sync="dialogPreviewPdfFile" style="width: 100%;height: 100%" fullscreen>
          <tcurrentProductlate v-if="isPdf">
              <vue-office-pdf
                      :src="previewUrl"
                      style="height: 100vh;"
              />
          </tcurrentProductlate>

      </el-dialog>

      <!--制定标准查看详情-->
      <el-dialog
              class="showInfo_dialog"
              :title="title_show"
              :visible.sync="dialogVisible_showInfo_Standard"
              width="520px"
              center
      >
          <el-form
                  :label-position="labelPosition"
                  label-width="80px"
                  :model="currentStandard"
                  style="margin-left: 20px">

              <el-form-item label="标准名称:" prop="name">
            <span>{{ currentStandard.name }}</span
            ><br/>
              </el-form-item>
              <el-form-item label="制定年月:" prop="date">
            <span>{{ currentStandard.date }}</span
            ><br/>
              </el-form-item>
              <el-form-item label="制定人:" prop="author">
            <span>{{ currentStandard.author }}</span
            ><br/>
              </el-form-item>
              <el-form-item label="作者人数:" prop="total">
            <span>{{ currentStandard.total }}</span
            ><br/>
              </el-form-item>
              <el-form-item label="作者排名:" prop="rank">
            <span>{{ currentStandard.rank }}</span
            ><br/>
              </el-form-item>
              <el-form-item label="成果积分:" prop="point">
            <span>{{ currentStandard.point }}</span
            ><br/>
              </el-form-item>
              <el-form-item label="成果状态:" prop="state">
            <span>{{
                currentStandard.state == "commit"
                    ? "已提交"
                    : currentStandard.state == "tea_pass"
                        ? "导师通过"
                        : currentStandard.state == "tea_reject"
                            ? "导师驳回"
                            : currentStandard.state == "adm_pass"
                                ? "管理员通过"
                                : "管理员驳回"
              }}</span
            ><br/>
              </el-form-item>
            <el-form-item label="证明材料:" prop="url">
              <span v-if="currentStandard.url == '' || currentStandard.url == null ? true : false">无证明材料</span>
              <div v-else>
                {{ currentStandard.url | fileNameFilter }}
                <el-button @click="previewMethod('1')" v-show="isImage || isPdf">预览</el-button>
                <el-button @click="previewMethod('10')">下载</el-button>
              </div>
              <div style="margin-top: 5px">
                <el-image
                    v-show="false"
                    ref="previewImage"
                    style="width: 100px; height: 100px"
                    :src="previewUrl"
                    :preview-src-list="previewImageSrcList">
                </el-image>
              </div>
            </el-form-item>
              <br/>
              <div>
                  <span>历史操作:</span>
                  <div
                          style="margin-top:10px;border:1px solid lightgrey;margin-left:2em;width:400px;height:150px;overflow:scroll">
                      <div v-for="item in operList" :key="item.time" style="margin-top:18px;color:gray;margin-left:5px">
                          <div>
                              <p>
                                  {{ item.time | dataFormat }}&nbsp;&nbsp;&nbsp;&nbsp;{{
                                  item.operatorName
                                  }}&nbsp;&nbsp;&nbsp;&nbsp;{{ item.operationName }}</p>
                              <p v-show="item.remark == '' || item.remark == null ? false : true">驳回理由：{{ item.remark }}</p>
                          </div>
                      </div>
                  </div>
              </div>
          </el-form>

          <span slot="footer" class="dialog-footer" :model="currentStandard">
            <el-button
                    id="but_pass"
                    v-show="(currentStandard.state == 'commit' || (currentStandard.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="(()=>{
                  if (role == 'teacher')
                   auditing_commit('tea_pass')
                  else if (role == 'admin')
                   auditing_commit('adm_pass')
                }) "
                    type="primary"
            >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentStandard.state == 'commit' || (currentStandard.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentStandard.state=='tea_reject' || currentStandard.state=='adm_reject' || currentStandard.state == 'adm_pass' || (currentStandard.state=='tea_pass' && role == 8))? true:false"
                    @click="dialogVisible_show = false"
                    type="primary"
            >关闭</el-button>
        </span>
      </el-dialog>
      <el-dialog v-model="currentStandard" :visible.sync="isShowInfo">
          <el-input
                  type="textarea"
                  :rows="4"
                  v-model="reason"
                  placeholder="请输入驳回理由"
          >
          </el-input>
          <span slot="footer">
          <el-button @click="rejectDialogConfirm()" type="primary">确定</el-button>
          <el-button @click="isShowInfo = false">取消</el-button>
        </span>
      </el-dialog>
      <el-dialog :visible.sync="dialogPreviewPdfFile" style="width: 100%;height: 100%" fullscreen>
          <tcurrentStandardlate v-if="isPdf">
              <vue-office-pdf
                      :src="previewUrl"
                      style="height: 100vh;"
              />
          </tcurrentStandardlate>

      </el-dialog>

  </div>
</template>

<script>
  import { set } from 'vue';
  import axios from "axios";
  export default {
    name: "SalSearch",
    data() {
      return {
          selectedItems: [],
          isImage: false,
        isPdf: false,
          isApproved: false,
        dialogPreviewPdfFile: false,
        previewImageSrcList: [],
        previewUrl: '',
        pointBack: '',
        pointFront: '',
        searchPatentState: '',
        searchPatentName: '',
        searchStudentName: '',
        pageSizes:[10, 20, 50, 100],
        totalCount:0,
        currentPage:1,
        pageSize:10,
        tmp1:'',tmp2:'',tmp3:'', //假装绑定了v-model，让控制台不报错
        ispubFlag:false,
        ispubShow:false,
        operList:[],
        isShowInfo:false,
        select_stuName:["全部"],//筛选框
        select_paperName:["全部"],
        select_point:['全部',0,1,3,4,6,9,12,15],
        select_pubName:[],
        option:["全部","学生提交","导师通过","管理员通过","导师驳回","管理员驳回"],
        labelPosition: "left",
        title: "",
        title_show: "",
        importDataBtnText: "导入数据",
        importDataBtnIcon: "el-icon-upload2",
        importDataDisabled: false,
        showAdvanceSearchView: false,
        copyemps:[],
        patents: [],
        loading: false,
        //学术论文
        emp: {
          id: null,
          institutionID: null,
          name: null,//论文名称
          year: "",
          month: "",
          rank: "",//排名
          total: "",//总人数
          content: "",
          url: '',
          state: '',
          pubPage: '',
          publicationId: null,
          startPage: '',
          endPage: ''
        },
        //项目开发
        currentProgram: {
          id: null,
          name: null,
          studentId: '',
          state: '',
          point: "",
          remark: '',
          indicatorId: '',
          workHours:'',
        },
        //授权专利
        currentPatent: {
          id: '',
          name: '',
          studentId: '',
          date: "",
          grantedStatus: '',
          author: "",
          rank: "",
          total: "",
          point: "",
          url: '',
          state: " ",
          remark: '',
          indicatorId: ''
        },
        // 科研获奖
        currentAward: {
          id: null,
          name: null,
          author: "",
          state: '',
          date: "",
          rank: "",
          total: "",
          point: "",
          url: '',
          remark: '',
          awardLevel: '',
          awardTypeId: '',
          awardType: {}
        },
        // 学术专著和教材
        currentMonograph: {
          id: null,
          name: null,
          author: "",
          state: '',
          date: "",
          rank: "",
          total: "",
          point: "",
          url: '',
          remark: '',
          publisher: '',
          isbn: ''
        },
          watch: {
              isApproved(newVal) {
                  if (newVal) {
                      // 如果审核通过，关闭所有详情弹窗
                      this.dialogVisible_showInfo_Paper = false;
                      this.dialogVisible_showInfo_Patent = false;
                      this.dialogVisible_showInfo_ResearchAward = false;
                      this.dialogVisible_showInfo_AcademicMonograph = false;
                      this.dialogVisible_showInfo_ResearchProject = false;
                      this.dialogVisible_showInfo_HorizontalResearchProject = false;
                      this.dialogVisible_showInfo_AcademicCompetition = false;
                      this.dialogVisible_showInfo_Decision = false;
                      this.dialogVisible_showInfo_Product = false;
                      this.dialogVisible_showInfo_Standard = false;

                      // 重置标志位
                      this.isApproved = false;
                  }
              },
          },
        //纵向科研项目 
        currentProject: {
          id: null,
          name: null,
          author: "",
          state: '',
          startDate: "",
          endDate: "",
          rank: "",
          total: "",
          point: "",
          url: '',
          remark: '',
          projectTypeId: '',
          projectType: {}
        },
        // 学科竞赛
        currentCompetition: {
          id: null,
          name: null,
          author: "",
          state: '',
          date: "",
          competitionLevel: '',
          rank: "",
          total: "",
          point: "",
          url: '',
          remark: '',
          competitionTypeId: '',
          competitionType: {}
        },
        // 决策咨询
        currentDecision : {
          id: null,
          name: null,
          author:"",
          state: '',
          date: "",
          rank: "",
          total:"",
          point:"",
          url:'',
          remark:'',
          decisionLevel:'',
          decisionTypeId:'',
          decisionType: {}
        },
        // 项目文档
        currentProduct: {
          id: '',
          name: '',
          studentId: '',
          date: "",
          author: "",
          rank: "",
          total: "",
          point: "",
          url: '',
          state: " ",
          remark: '',
          indicatorId: ''
        },
        // 制定标准
        currentStandard: {
          id: '',
          name: '',
          studentId: '',
          date: "",
          author: "",
          rank: "",
          total: "",
          point: "",
          url: '',
          state: " ",
          remark: '',
          indicatorId: ''
        },
        // 对话框显示状态

        dialogVisible_showInfo: false,
        // 学术论文查看详情按钮
        dialogVisible_showInfo_Paper: false,
        // 授权专利查看详情按钮
        dialogVisible_showInfo_Patent: false,
        // 科研获奖查看详情按钮
        dialogVisible_showInfo_ResearchAward: false,
        // 学术专著和教材查看详情按钮
        dialogVisible_showInfo_AcademicMonograph: false,
        // 纵向科研项目查看详情按钮
        dialogVisible_showInfo_ResearchProject: false,
        // 项目开发查看详情按钮
        dialogVisible_showInfo_HorizontalResearchProject: false,
        // 学科竞赛查看详情按钮
        dialogVisible_showInfo_AcademicCompetition: false,
        // 决策咨询查看详情按钮
        dialogVisible_showInfo_Decision: false,
        // 项目文档查看详情按钮
        dialogVisible_showInfo_Product: false,
        // 制定标准查看详情按钮
        dialogVisible_showInfo_Standard: false,

        dialogVisible: false,
        dialogVisible_pass: false,
        dialogVisible_reject: false,
        dialogVisible_show: false,
        positions: [],
          currentType: '',
          reason:"",
        oper:{
          operatorRole: "",
          operatorId: JSON.parse(localStorage.getItem('user')).id,
          operatorName: JSON.parse(localStorage.getItem('user')).name,
          prodType: '',
          operationName:"",
          state:"",
          remark:"",
          time: null,
          prodId: null,
        },
        // emp: {
        //   id: null,
        //   institutionID: null,
        //   name: null,
        //   startDate: "2022/02/02",
        //   scoreItemCount: "0",
        //   score: "100",
        //   groupCount: "0",
        //   expertCount: "0",
        //   participantCount: "0",
        //   comment: "",
        //   state:"",
        //   student:{},
        //   total:0,
        //   rank:0
        //   // reason:"",
        // },
      };
    },
    computed: {
      user() {
        return this.$store.state.currentHr; //object信息
      },
      menuHeight() {
        return this.select_pubName.length * 50 > 150
                ? 150 + 'px'
                : '${this.select_pubName.length * 50}px'
      },
      role() {
        // return JSON.parse(localStorage.getItem('user')).roleName.indexOf('teacher') >= 0 ||
        // JSON.parse(localStorage.getItem('user')).roleName.indexOf('expert') >= 0 ? 'teacher' : 'admin';
        return JSON.parse(localStorage.getItem('user')).roleName == 'expert' || JSON.parse(localStorage.getItem('user')).roleName == 'expert;' ?
                'expert' : JSON.parse(localStorage.getItem('user')).roleName.indexOf('teacher') >= 0 ?
                        'teacher' : JSON.parse(localStorage.getItem('user')).roleName.indexOf('admin') >= 0 ? 'admin' : '';
      }
    },
    created() {},
    mounted() {
      if(this.role == 'teacher') this.searchPatentState = '学生提交';
      else if(this.role == 'admin') this.searchPatentState = '导师通过';
      this.searchPatentListByCondicitions(1, 10);
    },
    methods: {

        handleSelectionChange(selection) {
            this.selectedItems = selection;
        },

        batchAudit() {
            if (this.selectedItems.length === 0) {
                this.$message.warning('请选择要审核的记录');
                return;
            }

            const auditPromises = this.selectedItems.map(item => {
                const state = 'adm_pass'; // 假设批量审核通过
                const urlMap = {
                    '学术论文': '/paper/basic/edit_state',
                    '授权专利': '/patent/basic/edit_state',
                    '科研获奖': '/award/basic/edit_state',
                    '学术专著和教材': '/monograph/basic/edit_state',
                    '纵向科研项目': '/project/basic/edit_state',
                    '项目开发': '/programRecord/basic/edit_state',
                    '学科竞赛': '/competition/basic/edit_state',
                    '决策咨询': '/decision/basic/edit_state',
                    '撰写项目文档': '/product/basic/edit_state',
                    '制定标准': '/standard/basic/edit_state'
                };

                const url = `${urlMap[item.type]}?state=${state}&ID=${item.id}`;
                return this.getRequest(url).then(response => {
                    if (response) {
                        this.doAddOper1(state, '', item.id, item.type); // 调用 doAddOper 方法记录日志
                    }
                });
            });

            Promise.all(auditPromises).then(responses => {
                this.$message.success('批量审核成功');
                this.selectedItems = []; // 清空选中的记录
                this.searchPatentListByCondicitions(this.currentPage, this.pageSize); // 刷新数据
            }).catch(error => {
                this.$message.error('批量审核失败');
                console.error(error);
            });
        },

        async doAddOper1(state, remark, id, type) {
            this.oper.state = state;
            this.oper.remark = remark;
            this.oper.prodId = id;
            this.oper.prodType = type;
            this.oper.time = this.dateFormatFunc(new Date());
            this.oper.operatorRole = this.role;
            if (this.oper.state == "tea_pass" || this.oper.state == 'adm_pass') {
                this.oper.operationName = "审核通过";
            } else if (this.oper.state == "tea_reject" || this.oper.state == 'adm_reject') {
                this.oper.operationName = "审核驳回";
            }
            await this.postRequest1("/oper/basic/add", this.oper);
        },



      previewMethod(type) {
        if(type == '1') {
          this.previewFileMethod(this.emp).then(res => {
            this.previewUrl = res;
            if(this.isImage) {
              this.previewImageSrcList = [res];
              this.$refs.previewImage.showViewer = true;
            }
            if(this.isPdf) {
              this.dialogPreviewPdfFile = true;
            }
          });
        }else if (type == '2'){//学术论文
            this.downloadFileMethod(this.emp);

        }else if (type == '3'){//授权专利
            this.downloadFileMethod(this.currentPatent);
        } else if (type == '4'){//科研获奖
            this.downloadFileMethod(this.currentAward);
        }
        else if (type == '5'){//学术专著和教材
            this.downloadFileMethod(this.currentMonograph);
        }
        else if (type == '6'){//纵向
            this.downloadFileMethod(this.currentProject);
        }
        else if (type == '7'){//学科竞赛
            this.downloadFileMethod(this.currentCompetition);
        }
        else if (type == '8'){//决策咨询
            this.downloadFileMethod(this.currentDecision);
        }
        else if (type == '9'){//项目文档
            this.downloadFileMethod(this.currentProduct);
        }
        else if (type == '10'){//指定标准
            this.downloadFileMethod(this.currentStandard);
        }

      },
      changePointMethod(data) { //修改积分按钮
        var have_score = data.have_score
        var point = data.point
        var score = have_score == 1 ? 0 : point
        this.$confirm(`确定将积分修改为${score}分?`,'提示',{
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        }).then(() => {
          this.changePaperPoint(data, score).then(() => {
            this.changeStudentPoint(data, score).then(() => {
              this.$message.success('修改成功！')
              data.have_score = 1 - data.have_score;
              if(data.have_score == 1) data.changePointButton = '取消积分'
              else if(data.have_score == 0) data.changePointButton = '计入积分'
            });
          })
        }).catch(() => {
          data.have_score = have_score;
        })
      },
      changePaperPoint(data, point) {
        const params = {
          point: point,
          have_score: 1 - data.have_score
        }
        return this.postRequest(`/patent/basic/editPoint/${data.id}`, params).then()
      },
      changeStudentPoint(data, point) {
        const params = {
          studentID: data.studentId,
          point: data.point //传递需要进行加法或减法的数值
        }
        if(point == 0) { //减法
          return this.postRequest('/graduatestudentM/basic/updateScoreSub', params).then()
        } else { //加法
          return this.postRequest('/graduatestudentM/basic/updateScore', params).then()
        }
      },
      rejectDialog() {
        if(this.role == 'admin' && this.emp.state == 'commit') { //管理员驳回 有提示
          this.$confirm('目前导师尚未审核，是否确认审核驳回？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.isShowInfo = true;
          }).catch(() => {});
        }else this.isShowInfo = true;
      },
      rejectDialogConfirm(){
        if (this.role == 'teacher')
          this.auditing_commit('tea_reject')
        else if (this.role == 'admin')
          this.auditing_commit('adm_reject')
        this.isShowInfo = false
      },
      //点击对话框中的确定按钮 触发事件
      auditing_commit(state){
        this.loading = true;
        if(this.role == 'admin' && (state.indexOf('pass') >= 0 || state.indexOf('reject') >= 0) && this.emp.state == 'commit') { //管理员通过 有提示
          this.$confirm('目前导师尚未审核，是否确认审核该成果？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            // type: 'warning'
          }).then(() => {
            if ("学术论文" == this.emp.type) {
              // 直接调用 rolePass
              this.rolePass1(state);
            }else if ("科研获奖" ==this.emp.type ) {
              // 直接调用 rolePass
              this.rolePass2(state);
            } else if ("纵向科研项目" == this.emp.type) {
              // 直接调用 rolePass
              this.rolePass3(state);
            }else if ("学科竞赛" == this.emp.type) {
              // 直接调用 rolePass
              this.rolePass4(state);
            }
            else if ("撰写项目文档" == this.emp.type) {
              // 直接调用 rolePass
              this.rolePass5(state);
            }
            else if ("授权专利" == this.emp.type) {
              // 直接调用 rolePass
              this.rolePass(state);
            }
            else if ("学术专著和教材" == this.emp.type) {
              // 直接调用 rolePass
              this.rolePass6(state);
            }
            else if ("项目开发" == this.emp.type) {
              // 直接调用 rolePass
              this.rolePass20(state);
            }
            else if ("决策咨询" == this.emp.type) {
              // 直接调用 rolePass
              this.rolePass8(state);
            }else if ("制定标准" == this.emp.type) {
              // 直接调用 rolePass
              this.rolePass9(state);
            }
          }).catch(() => {
            this.loading = false;
          });
        }else if ("学术论文" == this.currentType) {
          // 直接调用 rolePass
          this.rolePass1(state);
        }else if ("科研获奖" ==this.currentType ) {
          // 直接调用 rolePass
          this.rolePass2(state);
        } else if ("纵向科研项目" == this.currentType) {
          // 直接调用 rolePass
          this.rolePass3(state);
        }else if ("学科竞赛" == this.currentType) {
          // 直接调用 rolePass
          this.rolePass4(state);
        }
        else if ("撰写项目文档" == this.currentType) {
          // 直接调用 rolePass
          this.rolePass5(state);
        }
        else if ("授权专利" == this.currentType) {
          // 直接调用 rolePass
          this.rolePass(state);
        }
        else if ("学术专著和教材" == this.currentType) {
          // 直接调用 rolePass
          this.rolePass6(state);
        }
        else if ("项目开发" == this.currentType) {
          // 直接调用 rolePass
          this.rolePass20(state);
        }
        else if ("决策咨询" == this.currentType) {
          // 直接调用 rolePass
          this.rolePass8(state);
        }else if ("制定标准" == this.currentType) {
          // 直接调用 rolePass
          this.rolePass9(state);
        }
// 关闭所有弹窗
          this.closeAllDialogs();

      },

        closeAllDialogs() {
            this.dialogVisible_showInfo_Paper = false;
            this.dialogVisible_showInfo_Patent = false;
            this.dialogVisible_showInfo_ResearchAward = false;
            this.dialogVisible_showInfo_AcademicMonograph = false;
            this.dialogVisible_showInfo_ResearchProject = false;
            this.dialogVisible_showInfo_HorizontalResearchProject = false;
            this.dialogVisible_showInfo_AcademicCompetition = false;
            this.dialogVisible_showInfo_Decision = false;
            this.dialogVisible_showInfo_Product = false;
            this.dialogVisible_showInfo_Standard = false;
        },
      rolePass(state) {
        let url = "/patent/basic/edit_state?state=" + state + "&ID="+this.currentPatent.id;
        this.dialogVisible_show=false

        if(state.indexOf('reject') >= 0){
          this.currentPatent.operationList[0].remark = this.reason;
        }
        this.getRequest(url).then((resp) => {
          this.loading = false;
          if (resp) {
            this.currentPatent.state = state
            this.$message({
              type: 'success',
              message: '操作成功',
                isApproved :true
            })
              this.isApproved = true;
            this.doAddOper(state, this.reason, this.currentPatent.id,'授权专利');
            let roleParam = this.role.indexOf('admin') >= 0 ? 'admin' : this.role.indexOf('teacher') >= 0 ? 'teacher' : '';
            this.$store.dispatch('changePendingMessageange', roleParam);
          }
        })
      },
      rolePass1(state) {
        let url = "/paper/basic/edit_state?state=" + state + "&ID="+this.emp.id;
        this.dialogVisible_show=false// 关闭弹窗
        if(state.indexOf('reject') >= 0){
          this.emp.operationList[0].remark = this.reason;
        }
        this.getRequest(url).then((resp) => {
          this.loading = false;
          if (resp) {
            this.emp.state = state
              this.$message({
              type: 'success',
              message: '操作成功'
            })
              this.doAddOper(state, this.reason, this.emp.id,'学术论文');
              let roleParam = this.role.indexOf('admin') >= 0 ? 'admin' : this.role.indexOf('teacher') >= 0 ? 'teacher' : '';
              this.$store.dispatch('changePendingMessageange', roleParam);
          }

        })

      },
      rolePass2(state) {
        let url = "/award/basic/edit_state?state=" + state + "&ID="+this.currentAward.id;
        this.dialogVisible_show=false
        if(state.indexOf('reject') >= 0){
          this.currentAward.operationList[0].remark = this.reason;
        }
        this.getRequest(url).then((resp) => {
          this.loading = false;
          if (resp) {
            this.currentAward.state = state
            this.$message({
              type: 'success',
              message: '操作成功'
            })
            this.doAddOper(state, this.reason, this.currentAward.id,'科研获奖');
            let roleParam = this.role.indexOf('admin') >= 0 ? 'admin' : this.role.indexOf('teacher') >= 0 ? 'teacher' : '';
            this.$store.dispatch('changePendingMessageange', roleParam);
          }
        })
      },
        rolePass3(state) {
            let url = "/project/basic/edit_state?state=" + state + "&ID="+this.currentProject.id;
            this.dialogVisible_show=false
            if(state.indexOf('reject') >= 0){
                this.currentProject.operationList[0].remark = this.reason;
            }
            this.getRequest(url).then((resp) => {
                this.loading = false;
                if (resp) {
                    this.currentProject.state = state
                    this.$message({
                        type: 'success',
                        message: '操作成功'
                    })
                    this.doAddOper(state, this.reason, this.currentProject.id,'纵向科研项目');
                    let roleParam = this.role.indexOf('admin') >= 0 ? 'admin' : this.role.indexOf('teacher') >= 0 ? 'teacher' : '';
                    this.$store.dispatch('changePendingMessageange', roleParam);
                }
            })
        },
        rolePass20(state) {
            let url = "/programRecord/basic/edit_state?state=" + state + "&ID="+this.currentProgram.id;
            this.dialogVisible_show=false
            if(state.indexOf('reject') >= 0){
                this.currentProgram.operationList[0].remark = this.reason;
            }
            this.getRequest(url).then((resp) => {
                this.loading = false;
                if (resp) {
                    this.currentProgram.state = state
                    this.$message({
                        type: 'success',
                        message: '操作成功'
                    })
                    this.doAddOper(state, this.reason, this.currentProgram.id,'项目开发');
                    let roleParam = this.role.indexOf('admin') >= 0 ? 'admin' : this.role.indexOf('teacher') >= 0 ? 'teacher' : '';
                    this.$store.dispatch('changePendingMessageange', roleParam);
                }
            })
        },
        rolePass4(state) {
            let url = "/competition/basic/edit_state?state=" + state + "&ID="+this.currentCompetition.id;
            this.dialogVisible_show=false
            if(state.indexOf('reject') >= 0){
                this.currentCompetition.operationList[0].remark = this.reason;
            }
            this.getRequest(url).then((resp) => {
                this.loading = false;
                if (resp) {
                    this.currentCompetition.state = state
                    this.$message({
                        type: 'success',
                        message: '操作成功'
                    })
                    this.doAddOper(state, this.reason, this.currentCompetition.id,'学科竞赛');
                    let roleParam = this.role.indexOf('admin') >= 0 ? 'admin' : this.role.indexOf('teacher') >= 0 ? 'teacher' : '';
                    this.$store.dispatch('changePendingMessageange', roleParam);
                }
            })
        },
        rolePass5(state) {
            let url = "/product/basic/edit_state?state=" + state + "&ID="+this.currentProduct.id;
            this.dialogVisible_show=false
            if(state.indexOf('reject') >= 0){
                this.currentProduct.operationList[0].remark = this.reason;
            }
            this.getRequest(url).then((resp) => {
                this.loading = false;
                if (resp) {
                    this.currentProduct.state = state
                    this.$message({
                        type: 'success',
                        message: '操作成功'
                    })
                    this.doAddOper(state, this.reason, this.currentProduct.id,'撰写项目文档');
                    let roleParam = this.role.indexOf('admin') >= 0 ? 'admin' : this.role.indexOf('teacher') >= 0 ? 'teacher' : '';
                    this.$store.dispatch('changePendingMessageange', roleParam);
                }
            })
        },
        rolePass6(state) {
            let url = "/monograph/basic/edit_state?state=" + state + "&ID="+this.currentMonograph.id;
            this.dialogVisible_show=false
            if(state.indexOf('reject') >= 0){
                this.currentMonograph.operationList[0].remark = this.reason;
            }
            this.getRequest(url).then((resp) => {
                this.loading = false;
                if (resp) {
                    this.currentMonograph.state = state
                    this.$message({
                        type: 'success',
                        message: '操作成功'
                    })
                    this.doAddOper(state, this.reason, this.currentMonograph.id,'学术专著和教材');
                    let roleParam = this.role.indexOf('admin') >= 0 ? 'admin' : this.role.indexOf('teacher') >= 0 ? 'teacher' : '';
                    this.$store.dispatch('changePendingMessageange', roleParam);
                }
            })
        },
        rolePass8(state) {
            let url = "/decision/basic/edit_state?state=" + state + "&ID="+this.currentDecision.id;
            this.dialogVisible_show=false
            if(state.indexOf('reject') >= 0){
                this.currentDecision.operationList[0].remark = this.reason;
            }
            this.getRequest(url).then((resp) => {
                this.loading = false;
                if (resp) {
                    this.currentDecision.state = state
                    this.$message({
                        type: 'success',
                        message: '操作成功'
                    })
                    this.doAddOper(state, this.reason, this.currentDecision.id,'决策咨询');
                    let roleParam = this.role.indexOf('admin') >= 0 ? 'admin' : this.role.indexOf('teacher') >= 0 ? 'teacher' : '';
                    this.$store.dispatch('changePendingMessageange', roleParam);
                }
            })
        },
        rolePass9(state) {
            let url = "/standard/basic/edit_state?state=" + state + "&ID="+this.currentStandard.id;
            this.dialogVisible_show=false
            if(state.indexOf('reject') >= 0){
                this.currentStandard.operationList[0].remark = this.reason;
            }
            this.getRequest(url).then((resp) => {
                this.loading = false;
                if (resp) {
                    this.currentStandard.state = state
                    this.$message({
                        type: 'success',
                        message: '操作成功'
                    })
                    this.doAddOper(state, this.reason, this.currentStandard.id,'制定标准');
                    let roleParam = this.role.indexOf('admin') >= 0 ? 'admin' : this.role.indexOf('teacher') >= 0 ? 'teacher' : '';
                    this.$store.dispatch('changePendingMessageange', roleParam);
                }
            })
        },

      async doAddOper(state,remark,patentID,type) {
        this.oper.state = state;
        this.oper.remark = remark;
        this.oper.prodId = patentID;
        this.oper.prodType = type;
        this.oper.time = this.dateFormatFunc(new Date());
        this.oper.operatorRole = this.role;
        if(this.oper.state == "tea_pass" || this.oper.state == 'adm_pass'){
          this.oper.operationName = "审核通过"
        } else if (this.oper.state =="tea_reject" || this.oper.state == 'adm_reject'){
          this.oper.operationName = "审核驳回"
        }
        await this.postRequest1("/oper/basic/add", this.oper);
        await this.searchPatentListByCondicitions(this.currentPage, this.pageSize)
      },
      rowClass(){
        return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
      },
      emptyEmp() {
        this.emp = {
          id: null,
          startDate: null,
          name: "",
          scoreItemCount: "0",
          comment: "备注example：关于xxx的专利",
        };
      },
      showEditEmpView(data) {//修改论文
        this.title = "编辑单位信息";
        this.emp = data;
        this.dialogVisible = true;
      },
      showEditEmpView_show(data) {
        this.title_show = "显示详情";
        this.emp = data;
        this.dialogVisible_show = true;
        this.isPdf = this.isImage = false; //初始化
        this.previewUrl = '';
        this.previewImageSrcList = [];
        // if(data.url.includes('.pdf')) { //判断文件类型
        //   this.isPdf = true;
        // } else if(data.url.includes('.jpg') || data.url.includes('.png') || data.url.includes('.jpe') || data.url.includes('.JPG') || data.url.includes('.PNG') || data.url.includes('.JPE')) {
        //   this.isImage = true;
        // }
        this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=授权专利').then((resp) => {
          this.loading = false;
          if (resp) {
            this.isShowInfo = false;
            this.operList = resp.obj;
            this.operList.sort(function(a,b){
              return a.time > b.time ? -1 : 1
            })
          }
        });
      },

      showInfoMap(data) {
        const typeMap = {
          '学术论文': 'dialogVisible_showInfo_Paper',
          '授权专利': 'dialogVisible_showInfo_Patent',
          '科研获奖': 'dialogVisible_showInfo_ResearchAward',
          '学术专著和教材': 'dialogVisible_showInfo_AcademicMonograph',
          '纵向科研项目': 'dialogVisible_showInfo_ResearchProject',
          '项目开发': 'dialogVisible_showInfo_HorizontalResearchProject',
          '学科竞赛': 'dialogVisible_showInfo_AcademicCompetition',
          '决策咨询': 'dialogVisible_showInfo_Decision',
          '撰写项目文档': 'dialogVisible_showInfo_Product',
          '制定标准': 'dialogVisible_showInfo_Standard'
        }
        const dataMap = {
          '学术论文': 'emp',
          '授权专利': 'currentPatent',
          '科研获奖': 'currentAward',
          '学术专著和教材': 'currentMonograph',
          '纵向科研项目': 'currentProject',
          '项目开发': 'currentProgram',
          '学科竞赛': 'currentCompetition',
          '决策咨询': 'currentDecision',
          '撰写项目文档': 'currentProduct',
          '制定标准': 'currentStandard'
        }
        const urlMap = {
          '学术论文': '/paper',
          '授权专利': '/patent',
          '科研获奖': '/award',
          '学术专著和教材': '/monograph',
          '纵向科研项目': '/project',
          '项目开发': '/programRecord',
          '学科竞赛': '/competition',
          '决策咨询': '/decision',
          '撰写项目文档': '/product',
          '制定标准': '/standard'
        }
        if (typeMap[data.type]) {
          console.log(data.type + '显示详情:' + typeMap[data.type] + '--->' + dataMap[data.type])
          this.getRequest(urlMap[data.type] + "/basic/getDtaById?id=" + data.id).then((resp) => {
            this.loading = false;
            if (resp) {
              this[dataMap[data.type]] = resp.data;
            }
          });
          this[typeMap[data.type]] = true;
        }
      },
      // 显示项目详情对话框
      showInfo(data) {
        this.currentType = data.type; // 存储当前所选数据的 type
        this.handleShowInfo(data);
      },
      handleShowInfo(data) {

        this.title_show = "显示详情";
        console.log(data)
        this.currentProjectSummary = data
        this.showInfoMap(data)
        this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=' + data.type).then((resp) => {
          this.loading = false;
          if (resp) {
            this.operList = resp.obj
          }
        });
        if(data.type==='项目开发')
          return;
        this.isPdf = this.isImage = false; //初始化
        this.previewUrl = '';
        this.previewImageSrcList = [];
        this.isDataUrl(data)
      },

      isDataUrl(data) {
        if (data.url != null) {
          if (data.url.includes('.pdf')) { //判断文件类型
            this.isPdf = true;
          } else if (data.url.includes('.jpg') || data.url.includes('.png') || data.url.includes('.jpe') || data.url.includes('.JPG') || data.url.includes('.PNG') || data.url.includes('.JPE')) {
            this.isImage = true;
          }
        }
      },


      //应该要分是否有无筛选条件
      sizeChange(currentSize) {
        this.pageSize = currentSize;
        this.searchPatentListByCondicitions(this.currentPage, this.pageSize)
      },
      currentChange(currentPage) {
        this.currentPage = currentPage;
        this.searchPatentListByCondicitions(this.currentPage, this.pageSize)
      },
      searchPatentListByCondicitions(pageNum, pageSize) {//根据条件搜索论文
        const params = {};
        params.studentName = this.searchStudentName;
        var state = this.searchPatentState;
        if(state == '导师通过'){
          state = 'tea_pass'
        }else if(state == '导师驳回'){
          state = 'tea_reject'
        }else if(state == '学生提交'){
          state = 'commit'
        }else if (state == '管理员通过'){
          state = 'adm_pass'
        }else if (state == '管理员驳回') {
          state = 'adm_reject'
        }else state = '';
        if(this.pointFront == '全部') {
          params.pointFront = '';
        }else {
          params.pointFront = this.pointFront;
        }
        if(this.pointBack == '全部') {
          params.pointBack = '';
        }else {
          params.pointBack = this.pointBack;
        }
        params.state = state;
        params.name = this.searchPatentName;
        params.pageNum = pageNum.toString();
        params.pageSize = pageSize.toString();
        this.postRequest('/xinproject/basic/searchProjectByConditions', params).then((response) => {
          if(response) {
            this.patents = response.extend.res[0];
            this.patents.map(item => {
              if(item.have_score == 1) {
                this.$set(item, 'changePointButton', '取消积分')
              } else {
                this.$set(item, 'changePointButton', '计入积分')
              }
            })
            this.totalCount = response.extend.res[1];
          }else this.projectList = [];
        })
      }

    },
  };
</script>

<style>
  .showInfo_dialog .el-form-item{
    margin-bottom: 5px;
  }
  .select_div_input{
    margin-left:3px;
    width:120px;
    height:32px;
    position:relative;
    display:inline-block
  }
  .select_div{
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
  input[type=text]::placeholder {
    color:lightgrey;
  }
  input:focus{
    border:1px solid lightblue;
  }
  .slide-fade-enter-active {
    transition: all 0.8s ease;
  }

  .slide-fade-leave-active {
    transition: all 0.8s cubic-bezier(1, 0.5, 0.8, 1);
  }

  .slide-fade-enter, .slide-fade-leave-to
    /* .slide-fade-leave-active for below version 2.1.8 */ {
    transform: translateX(10px);
    opacity: 0;
  }
  div::-webkit-scrollbar {
    /* 隐藏默认的滚动条 */
    -webkit-appearance: none;
  }
  div::-webkit-scrollbar:vertical {
    /* 设置垂直滚动条宽度 */
    width: 6px;
  }


  /* 这里不需要用到这个 */
  /* div::-webkit-scrollbar:horizontal{ */
  /* 设置水平滚动条厚度 */
  /* height: 2px; */
  /* } */

  div::-webkit-scrollbar-thumb {
    /* 滚动条的其他样式定制，注意，这个一定也要定制，否则就是一个透明的滚动条 */
    border-radius: 8px;
    border: 3px solid rgba(255,255,255,.4);
    background-color: rgba(0, 0, 0, .5);
  }
</style>
