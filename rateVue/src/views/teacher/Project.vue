<template>
    <div>
        <h2 >成果列表</h2>
        <div v-if="showSearch">
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

            <label style="margin-left:20px;">状态：</label>
            <el-select
                    v-model="searchPatentState"
                    style="margin-left:3px;width:120px"
                    prefix-icon="el-icon-edit"
                    clearable
                    filterable
                    placeholder="状态筛选"
            >
                <el-option
                        v-for="val in option"
                        :key="val"
                        :value="val"
                >
                </el-option>
            </el-select>
            <label style="margin-left:16px">积分范围：</label>
            <el-select
                    v-model="pointFront"
                    style="margin-left:3px;width:60px"
                    prefix-icon="el-icon-edit"
                    clearable
                    filterable
                    placeholder="0"
            >
                <el-option
                        style=""
                        v-for="val in select_point"
                        :key="val"
                        :value="val"
                >
                </el-option>
            </el-select>
            <label >&nbsp; - &nbsp;</label>
            <el-select
                    v-model="pointBack"
                    style="margin-left:3px;width:60px"
                    prefix-icon="el-icon-edit"
                    clearable
                    filterable
                    placeholder="12"
            >
                <el-option
                        style=""
                        v-for="val in select_point"
                        :key="val"
                        :value="val"
                >
                </el-option>
            </el-select>
            <el-button
                    icon="el-icon-search"
                    type="primary"
                    @click="initEmps()"
                    :disabled="showAdvanceSearchView"
                    style="margin-left:30px"
            >
                搜索
            </el-button>
        </div>
        <div style="margin-top: 10px;">
            <el-table
                    :data="emps"
                    stripe
                    border
                    v-loading="loading"
                    :header-cell-style="rowClass"
                    element-loading-text="正在加载..."
                    element-loading-spinner="el-icon-loading"
                    element-loading-background="rgba(0, 0, 0, 0.12)"
                    style="width: 100%;"
            >
                <el-table-column
                        fixed
                        type="index"
                        label="序号"
                        align="center"
                        min-width="8%"
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
                        prop="applyTime"
                        label="操作时间"
                        min-width="15%"
                        align="center"
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
                        prop="category"
                        label="类别"
                        align="center"
                        min-width="10%"
                >
                </el-table-column>
                <el-table-column
                        prop="participants"
                        align="center"
                        label="参与人"
                        min-width="15%"
                >
                </el-table-column>
                <el-table-column
                        prop="state"
                        label="状态"
                        min-width="10%"
                        align="center"
                >
                    <template slot-scope="scope">
              <span
                      style="padding: 4px"
                      :style="(scope.row.status=='tea_reject' || scope.row.status=='adm_reject') ? {'color':'red'}:{'color':'gray'}"
                      size="mini"
              >
                {{
                  scope.row.status == "commit"
                      ? "学生提交"
                      : scope.row.status == "tea_pass"
                          ? "导师通过"
                          : scope.row.status == "tea_reject"
                              ? "导师驳回"
                              : scope.row.status == "adm_pass"
                                  ? "管理员通过"
                                  : "管理员驳回"
                }}
                </span>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="remark"
                        min-width="20%"
                        align="center"
                        label="备注"
                >
                </el-table-column>
                <el-table-column
                        prop="point"
                        label="积分"
                        align="center"
                        min-width="8%"
                        :formatter="formatPoint"
                >
                </el-table-column>
                <el-table-column align="center" width="280px" label="操 作" min-width="20%">
                    <template slot-scope="scope">
                        <!--            @click="showEditEmpView(scope.row, scope.$index)"-->
                        <!--              <el-button-->
                        <!--                  @click="showEditEmpView(scope.row)"-->
                        <!--                  style="padding: 4px"-->
                        <!--                  size="mini"-->
                        <!--                  icon="el-icon-edit"-->
                        <!--                  type="primary"-->
                        <!--                  plain-->
                        <!--                  v-show="scope.row.status == 'commit' || scope.row.status == 'tea_reject' || scope.row.status == 'adm_reject'? true:false"-->
                        <!--              >编辑-->
                        <!--              </el-button-->
                        <!--              >-->
                        <!-- 如果 status 为 "commit"，显示审核按钮 -->
                        <el-button
                                v-if="scope.row.status === 'commit'"
                                @click="showInfo(scope.row)"
                                style="padding: 4px"
                                size="mini"
                        >审核
                        </el-button>

                        <!-- 如果 status 不是 "commit"，显示查看详情按钮 -->
                        <el-button
                                v-else
                                @click="showInfo(scope.row)"
                                style="padding: 4px"
                                size="mini"
                        >查看详情
                        </el-button>

                        <!--              <el-button-->
                        <!--                  @click="deleteEmp(scope.row)"-->
                        <!--                  style="padding: 4px"-->
                        <!--                  size="mini"-->
                        <!--                  type="danger"-->
                        <!--                  icon="el-icon-delete"-->
                        <!--                  plain-->
                        <!--                  v-show="scope.row.status == 'tea_reject' || scope.row.status == 'commit'? true:false"-->
                        <!--              >删除-->
                        <!--              </el-button-->
                        <!--              >-->
                    </template>
                </el-table-column>
            </el-table>
        </div>
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
                    ? "学生提交"
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
                <el-form-item label="发表年月:">
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
            <span slot="footer" class="dialog-footer">
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
                    ? "学生提交"
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
            <span slot="footer" class="dialog-footer">
         <el-button
                 id="but_pass"
                 v-show="(currentPatent.state == 'commit' || (currentPatent.state == 'tea_pass' && role == 'admin')) ? true : false"
                 @click="(()=>{
                   auditing_commit('tea_pass')
                }) "
                 type="primary"
         >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentPatent.state == 'commit' || (currentPatent.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
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
                    ? "学生提交"
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

            <span slot="footer" class="dialog-footer">
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

            <span slot="footer" class="dialog-footer">
<!--          <el-button type="primary" @click="dialogVisible_showInfo_AcademicMonograph = false"-->
<!--          >关 闭-->
<!--          </el-button>-->
            <el-button
                    id="but_pass"
                    v-show="(currentMonograph.state == 'commit' || (currentMonograph.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="(()=>{
                   auditing_commit('tea_pass')
                }) "
                    type="primary"
            >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentMonograph.state == 'commit' || (currentMonograph.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
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
<!--        <el-dialog :visible.sync="dialogPreviewPdfFile" style="width: 100%;height: 100%" fullscreen>-->
<!--            <template v-if="isPdf">-->
<!--                <vue-office-pdf-->
<!--                        :src="previewUrl"-->
<!--                        style="height: 100vh;"-->
<!--                />-->
<!--            </template>-->

<!--        </el-dialog>-->










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

            <span slot="footer" class="dialog-footer">
         <el-button
                 id="but_pass"
                 v-show="(currentProject.state == 'commit' || (currentProject.state == 'tea_pass' && role == 'admin')) ? true : false"
                 @click="(()=>{
                   auditing_commit('tea_pass')
                }) "
                 type="primary"
         >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentProject.state == 'commit' || (currentProject.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
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

            <el-form-item label="成果名称:" prop="name">
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
                    ? "学生提交"
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
            <span slot="footer" class="dialog-footer">
            <el-button
                    id="but_pass"
                    v-show="(currentProgram.state == 'commit' || (currentProgram.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="(()=>{
                   auditing_commit('tea_pass')
                }) "
                    type="primary"
            >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentProgram.state == 'commit' || (currentProgram.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
        </span>
        </el-dialog>
        <el-dialog v-model="currentProgram" :visible.sync="isShowInfo">
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
                    ? "学生提交"
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
                        {{ currentCompetition.url | fileNameFilter }}
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

            <span slot="footer" class="dialog-footer">
           <el-button
                   id="but_pass"
                   v-show="(currentCompetition.state == 'commit' || (currentCompetition.state == 'tea_pass' && role == 'admin')) ? true : false"
                   @click="(()=>{
                   auditing_commit('tea_pass')
                }) "
                   type="primary"
           >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentCompetition.state == 'commit' || (currentCompetition.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
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
                ? "学生提交"
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

            <span slot="footer" class="dialog-footer">
         <el-button
                 id="but_pass"
                 v-show="(currentDecision.state == 'commit' || (currentDecision.state == 'tea_pass' && role == 'admin')) ? true : false"
                 @click="(()=>{
                   auditing_commit('tea_pass')
                }) "
                 type="primary"
         >审核通过</el-button>
            <el-button
                    id="but_reject"
                    v-show="(currentDecision.state == 'commit' || (currentDecision.state == 'tea_pass' && role == 'admin')) ? true : false"
                    @click="rejectDialog"
                    type="primary"
            >审核不通过</el-button>
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
                    ? "学生提交"
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
            <span slot="footer" class="dialog-footer">
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
                    ? "学生提交"
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
            <span slot="footer" class="dialog-footer">
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

    import axios from "axios";
    import {postRequest1} from "@/utils/api";
    import {debounce} from "@/utils/debounce";

    export default {
        name: "Project",
        data() {
            return {
                // 项目列表数据
                emps: [],
                data: [],
                showSearch:true,
                reason:"",
                isShowInfo:false,
                role:"teacher",
                currentType: '',
                projects: [], // 用于存储项目数据
                showAdvanceSearchView: false,
                name:'',
                pointBack: '',
                state: '',
                studentName:'',
                pointFront: '',
                searchPatentState: '',
                searchPatentName: '',
                searchStudentName: '',
                selectedIndicator: {},

                oper: {
                    operatorRole: "student",
                    operatorId: JSON.parse(localStorage.getItem('user')).id,
                    operatorName: JSON.parse(localStorage.getItem('user')).name,
                    prodType: '授权专利',
                    operationName: '',
                    state: '',
                    remark: '',
                    prodId: null,
                    time: null
                },
                select_point:['全部',0,1,2,3,4,6,9,12,15],
                option:["全部","学生提交","导师通过","管理员通过","导师驳回","管理员驳回"],
                showTree: false, // 控制树形组件的显示与隐藏
                publish: {
                    id: '',
                    publicationId: '',
                    indicatorId: '',
                    indicatorName: '',
                    year: '',
                    student_id: '',
                    date: '',
                    state: '',
                    publicationName: '',
                    publicationAbbr: '',
                    publisherName: '',
                    publicationUrl: '',
                    publicationProofUrl: ''
                },
                buttonText: '点击选择指标点分类',
                dialogVisible_publication: false,
                title_publication: "",
                publication_detail: "",
                // 加载状态
                loading: false,
                // 对话框显示状态
                dialogVisible: false,
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
                // 横向科研项目查看详情按钮
                dialogVisible_showInfo_HorizontalResearchProject: false,
                // 学科竞赛查看详情按钮
                dialogVisible_showInfo_AcademicCompetition: false,
                // 决策咨询查看详情按钮
                dialogVisible_showInfo_Decision: false,
                // 项目文档查看详情按钮
                dialogVisible_showInfo_Product: false,
                // 制定标准查看详情按钮
                dialogVisible_showInfo_Standard: false,

                // 学术论文编辑显示
                dialogVisible_publication_Paper: false,
                // 授权专利编辑显示
                dialogVisible_publication_Patent: false,
                // 科研获奖编辑显示
                dialogVisible_publication_ResearchAward: false,
                // 学术专著和教材编辑显示
                dialogVisible_publication_AcademicMonograph: false,
                // 纵向科研项目编辑显示
                dialogVisible_publication_ResearchProject: false,
                // 横向科研项目编辑显示
                dialogVisible_publication_HorizontalResearchProject: false,
                // 学科竞赛编辑显示
                dialogVisible_publication_AcademicCompetition: false,
                // 决策咨询编辑显示
                dialogVisible_publication_Decision: false,
                // 项目文档编辑显示
                dialogVisible_publication_Product: false,
                // 制定标准编辑显示
                dialogVisible_publication_Standard: false,

                showTreeDialog: false,
                dialogPreviewPdfFile: false,
                // 表单标题
                title: '添加项目',
                title_show: '项目详情',
                // 表单标签位置
                labelPosition: 'right',
                // 当前项目副本
                paperPoint: 0,
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
                //横向科研项目
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
                //纵向科研项目 横向科研项目
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
                // 学术论文编辑数据
                currentEmp: {},
                //授权专利编辑数据
                patentStatusList: [],
                disabledSelectAwardType: true,
                patentPoint: 0,
                selectAwardTypeList: [],
                patentStatusListObject: [
                    {
                        name: '受理',
                        value: 0
                    }, {
                        name: '初审',
                        value: 1
                    }, {
                        name: '公布',
                        value: 2
                    },
                    {
                        name: '实审',
                        value: 3
                    }, {
                        name: '授权',
                        value: 4
                    }, {
                        name: '转让',
                        value: 5
                    }],
                currentPatentCopy: {},
                //科研获奖编辑数据
                selectAwardType: {},
                awardLevelList: [
                    {
                        label: '国家级',
                        value: 1
                    },
                    {
                        label: '省部级',
                        value: 2
                    }
                ],
                awardPoint: 0,
                currentAwardCopy: {},
                //学术专著和教材编辑数据
                currentMonographCopy: {},
                addButtonState: false,
                monographPoint: 0,
                //纵向科研项目编辑数据
                currentProjectCopy: {},
                disabledSelectProjectType: true,
                selectProjectType: '',
                selectProjectTypeList: [],
                //横向科研项目编辑数据
                currentHorizonProjectCopy: {},
                //学科竞赛项目编辑数据
                standardPoint: 0,
                currentCompetitionCopy: {},
                disabledSelectCompetitionType: true,
                selectCompetitionType: '',
                selectCompetitionTypeList: [],
                competitionPoint: 0,
                competitionLevelList: ['全国一等奖', '全国二等奖', '全国三等奖', '省部级一等奖', '省部级二等奖'],
                //决策咨询编辑数据
                currentDecisionCopy: {},
                selectDecisionType: {},
                selectDecisionTypeList: [],
                decisionPoint: 0,
                //项目文档编辑数据
                currentProductCopy: {},
                productPoint: 0,
                //制定标准编辑数据
                currentStandardCopy: {},
                // // 表单验证规则
                // 学术论文编辑表单验证
                rules_Paper: {
                    name: [{required: true, message: "请输入论文名", trigger: "blur"}],
                    author: [{required: true, message: "请输入作者列表", trigger: "blur"}],
                    year: [{required: true, message: "请选择发表年月", trigger: "blur"}]
                },
                // 授权专利编辑表单验证
                rules_Patent: {
                    name: [{required: true, message: "请输入专利名称", trigger: "blur"}],
                    author: [{required: true, message: "请输入专利作者", trigger: "blur"}],
                    date: [{required: true, message: "请选择完成时间", trigger: "blur"}],
                    grantedStatus: [{required: true, message: "请选择专利状态", trigger: "blur"}]
                },
                // 科研获奖编辑表单验证
                rules_ResearchAward: {
                    name: [{required: true, message: "请输入奖励名称", trigger: "blur"}],
                    awardClass: [{required: true, message: "请输入奖励类别", trigger: "blur"}],
                    awardLevel: [{required: true, message: "请输入奖励级别", trigger: "blur"}]
                },
                //学术专著和教材编辑表单验证
                rules_Monograph: {
                    name: [{required: true, message: "请输入专著或教材名称", trigger: "blur"}],
                    publisher: [{required: true, message: "请输入专著或教材出版社", trigger: "blur"}],
                    isbn: [{required: true, message: "请输入专著或教材ISBN", trigger: "blur"}]
                },
                //纵向科研项目编辑表单验证
                rules_ResearchProject: {
                    name: [{required: true, message: "请输入科研项目名称", trigger: "blur"}],
                    startDate: [{required: true, message: "请输入科研项目立项年月", trigger: "blur"}],
                    author: [{required: true, message: "请输入科研项目作者", trigger: "blur"}],
                },
                //横向科研项目编辑表单验证
                rules_HorizonResearchProject: {
                    name: [{required: true, message: "请输入项目名称", trigger: "blur"}],
                    author: [{required: true, message: "请输入项目作者", trigger: "blur"}],
                    startDate: [{required: true, message: "请选择立项时间", trigger: "blur"}]
                },
                //学科竞赛项目编辑表单验证
                rules_Competition: {
                    name: [{required: true, message: "请输入学科竞赛名称", trigger: "blur"}],
                    date: [{required: true, message: "请输入学科竞赛获奖年月", trigger: "blur"}],
                    author: [{required: true, message: "请输入学科竞赛获奖人", trigger: "blur"}],
                    competitionLevel: [{required: true, message: "请选择学科竞赛级别", trigger: "blur"}]
                },
                //决策咨询编辑表单验证
                rules_Decision: {
                    name: [{required: true, message: "请输入决策名称", trigger: "blur"}]
                },
                //项目文档编辑表单验证
                rules_Product: {
                    name: [{required: true, message: "请输入文档名称", trigger: "blur"}],
                    author: [{required: true, message: "请输入文档作者", trigger: "blur"}],
                    date: [{required: true, message: "请选择完成时间", trigger: "blur"}]
                },
                //制定标准编辑表单验证
                rules_standard: {
                    name: [{required: true, message: "请输入标准名称", trigger: "blur"}],
                    author: [{required: true, message: "请输入标准制定人", trigger: "blur"}],
                    date: [{required: true, message: "请选择制定年月", trigger: "blur"}],
                },
                rulesPublication: {
                    publicationName: [
                        {required: true, message: '请输入期刊全称', trigger: 'blur'}
                    ],
                    publicationAbbr: [
                        {required: true, message: '请输入刊物简称', trigger: 'blur'}
                    ],
                    publisherName: [
                        {required: true, message: '请输入出版社', trigger: 'blur'}
                    ],
                    publicationUrl: [
                        {required: true, message: '请输入网址', trigger: 'blur'}
                    ],
                    year: [
                        {required: true, message: '请输入录入年份', trigger: 'blur'},
                    ],
                },
                // 搜索项目类
                searchTypeLoading: false,
                filesPublication: [], // 这里上传的是期刊的证明材料
                // 文件上传相关
                files: [],
                headers: {
                    Authorization: 'Bearer your_token'
                },
                // 当前项目
                // currentProject: {},
                // 操作列表
                operList: [],
                // 预览相关
                previewUrl: '',
                previewImageSrcList: [],
                isImage: false,
                isPdf: false,
                // 指标点数据
                indicatorData: [],
                defaultProps: {
                    children: 'children',
                    label: 'label'
                },
                defaultExpandedKeys: [],
                indicatorBtn: '请选择指标点',
                currentIndicator: {},
                disabledSelectDecisionType: false,
                zeroPointReason: '',
                cannotAddPublish: true,
            }
        },
        computed: {
            user() {
                return JSON.parse(localStorage.getItem('user')); //object信息
            },
        },
        mounted() {
            //this.currentProjectSummaryCopy = JSON.parse(JSON.stringify(this.currentProduct));
            const username = decodeURIComponent(this.$route.query.username || '');
            if (username) {
                this.searchStudentName = username;
                this.initEmps(); // 使用这个 username 去查询数据
            } else {
                this.initEmps();// 默认加载所有数据
            }
            const showSearchQuery = this.$route.query.showSearch;
            this.showSearch = showSearchQuery === 'false' ? false : true
            Boolean(showSearchQuery);
            // this.initEmps();
            // this.showAddEmpView()
            // this.getData();
        },
        created() {
            // this.fetchProjects(); // 在组件创建时获取数据
        },
        methods: {
            rejectDialogConfirm(){
                if (this.role == 'teacher')
                    this.auditing_commit('tea_reject')
                else if (this.role == 'admin')
                    this.auditing_commit('adm_reject')
                this.isShowInfo = false
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
                        this.prodType =this.currentPatent.type;
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
                    console.log("==========="+  this.currentProject.operationList[0])
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
                        this.doAddOper(state, this.reason,this.currentMonograph.id,'学术专著和教材');
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


            fetchProjects() {
                this.loading = true;

                const params = {}; // 定义 params 变量
                this.postRequest('/project/data/basic/teacherOrAdmin',params).then((resp) => {
                    this.loading = false;
                    if (resp) {
                        this.projects = resp.data;
                    }
                }).catch(() => {
                    this.loading = false;
                    this.$message.error('获取项目数据失败');
                });



                // const url = '/project/data/basic/studentID?studentID=' + this.user.id;
                // this.getRequest(url).then((resp) => {
                //   this.loading = false;
                //   if (resp) {
                //     this.projects = resp.data;
                //   }
                // }).catch(() => {
                //   this.loading = false;
                //   this.$message.error('获取项目数据失败');
                // });
            },

            getRequest(url) {
                // 假设这是一个获取数据的方法
                return axios.get(url, {
                    headers: {
                        token: this.user ? this.user.token : '',
                    },
                });
            },
            dateFormatFunc(date) {
                // 假设这是一个日期格式化的方法
                // return date.toISOString().replace('T', ' ').slice(0, 23) + isoString.slice(19);
                return date.toISOString().replace('T', ' ').slice(0, 19);
            },
            // 学术论文
            deletePaperEmpMethod(data) {
                return new Promise((resolve, reject) => {
                        this.deleteRequest("/paper/basic/remove/" + data.id).then((resp) => {
                            this.dialogVisible = false;
                            resolve('success');
                        })
                    }
                )
            },
            deletePaperOperationList(data) {
                const params = {}
                params.prodId = data.id;
                params.prodType = '学术论文'
                return new Promise((resolve, reject) => {
                    this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
                        resolve('success');
                    })
                })
            },
            addData() {
                // 假设这里是添加数据的请求
                // 模拟一个异步请求
                this.loading = true;
                setTimeout(() => {
                    // 模拟添加数据成功
                    // 这里应该是实际的添加数据请求成功后的回调
                    this.loading = false;
                    // 添加成功后，调用获取数据列表的方法
                    this.getData();
                }, 1000);
            },
            // 获取数据列表的方法
            getData() {
                this.loading = true;

            },
            // 授权专利
            deletePatentEmpMethod(data) {
                return new Promise((resolve, reject) => {
                        this.deleteRequest("/patent/basic/remove/" + data.id).then((resp) => {
                            this.dialogVisible = false;
                            resolve('success');
                        })
                    }
                )
            },
            deletePatentOperationList(data) {
                const params = {}
                params.prodId = data.id;
                params.prodType = '授权专利'
                return new Promise((resolve, reject) => {
                    this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
                        resolve('success');
                    })
                })
            },

            // 科研获奖
            deleteAwardEmpMethod(data) {
                return new Promise((resolve, reject) => {
                        this.deleteRequest("/award/basic/remove/" + data.id).then((resp) => {
                            this.dialogVisible = false;
                            resolve('success');
                        })
                    }
                )
            },
            deleteAwardOperationList(data) {
                const params = {}
                params.prodId = data.id;
                params.prodType = '科研获奖'
                return new Promise((resolve, reject) => {
                    this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
                        resolve('success');
                    })
                })
            },

            // 学术专著和教材
            deleteMonographEmpMethod(data) {
                return new Promise((resolve, reject) => {
                        this.deleteRequest("/monograph/basic/remove/" + data.id).then((resp) => {
                            this.dialogVisible = false;
                            resolve('success');
                        })
                    }
                )
            },
            deleteMonographOperationList(data) {
                const params = {}
                params.prodId = data.id;
                params.prodType = '学术专著和教材'
                return new Promise((resolve, reject) => {
                    this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
                        resolve('success');
                    })
                })
            },

            // 纵向科研项目
            deleteProjectEmpMethod(data) {
                return new Promise((resolve, reject) => {
                        this.deleteRequest("/project/basic/remove/" + data.id).then((resp) => {
                            this.dialogVisible = false;
                            resolve('success');
                        })
                    }
                )
            },
            deleteProjectOperationList(data) {
                const params = {}
                params.prodId = data.id;
                params.prodType = '纵向科研项目'
                return new Promise((resolve, reject) => {
                    this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
                        resolve('success');
                    })
                })
            },

            // 横向科研项目
            deleteHorizontalProjectEmpMethod(data) {
                return new Promise((resolve, reject) => {
                        this.deleteRequest("/project/basic/remove/" + data.id).then((resp) => {
                            this.dialogVisible = false;
                            resolve('success');
                        })
                    }
                )
            },
            deleteHorizontalProjectOperationList(data) {
                const params = {}
                params.prodId = data.id;
                params.prodType = '项目开发'
                return new Promise((resolve, reject) => {
                    this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
                        resolve('success');
                    })
                })
            },

            // 学科竞赛
            deleteCompetitionEmpMethod(data) {
                return new Promise((resolve, reject) => {
                        this.deleteRequest("/competition/basic/remove/" + data.id).then((resp) => {
                            this.dialogVisible = false;
                            resolve('success');
                        })
                    }
                )
            },
            deleteCompetitionOperationList(data) {
                const params = {}
                params.prodId = data.id;
                params.prodType = '学科竞赛'
                return new Promise((resolve, reject) => {
                    this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
                        resolve('success');
                    })
                })
            },

            // 决策咨询
            deleteDecisionEmpMethod(data) {
                return new Promise((resolve, reject) => {
                        this.deleteRequest("/decision/basic/remove/" + data.id).then((resp) => {
                            this.dialogVisible = false;
                            resolve('success');
                        })
                    }
                )
            },
            deleteDecisionOperationList(data) {
                const params = {}
                params.prodId = data.id;
                params.prodType = '决策咨询'
                return new Promise((resolve, reject) => {
                    this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
                        resolve('success');
                    })
                })
            },

            // 项目文档
            deleteProductEmpMethod(data) {
                return new Promise((resolve, reject) => {
                        this.deleteRequest("/product/basic/remove/" + data.id).then((resp) => {
                            this.dialogVisible = false;
                            resolve('success');
                        })
                    }
                )
            },
            deleteProductOperationList(data) {
                const params = {}
                params.prodId = data.id;
                params.prodType = '撰写项目文档'
                return new Promise((resolve, reject) => {
                    this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
                        resolve('success');
                    })
                })
            },
            // 制定标准
            deleteStandardEmpMethod(data) {
                return new Promise((resolve, reject) => {
                        this.deleteRequest("/standard/basic/remove/" + data.id).then((resp) => {
                            this.dialogVisible = false;
                            resolve('success');
                        })
                    }
                )
            },
            deleteStandardOperationList(data) {
                const params = {}
                params.prodId = data.id;
                params.prodType = '制定标准'
                return new Promise((resolve, reject) => {
                    this.postRequest('/oper/basic/deleteOperationList', params).then(res => {
                        resolve('success');
                    })
                })
            },
            showTreeDialog01() {
                this.dialogVisible_publication = false;
                this.showTree = true;
                this.initTree()
            },
            // 初始化树
            initTree() {
                var that = this;
                this.getRequest("/indicator").then(function (resp) {
                    //此处可以让父组件向子组件传递url,提高复用性
                    that.id = resp.obj[0];
                    that.data = resp.obj[1];
                });
            },
            emptyPublish() {
                this.inputDisabled = false
                this.publish = {
                    id: '',
                    publicationId: '',
                    indicatorId: '',
                    indicatorName: '',
                    year: '',
                    student_id: '',
                    date: '',
                    state: '',
                    publicationName: '',
                    publicationAbbr: '',
                    publisherName: '',
                    publicationUrl: '',
                    publicationProofUrl: ''
                };
                this.publishToDatabase = {
                    id: '',
                    publicationId: '',
                    indicatorName: '',
                    check_duplicates: {
                        indicatorId: [],
                        year: [],
                    },
                    student_id: '',
                    date: '',
                    state: '',
                    publicationName: '',
                    publicationAbbr: '',
                    publisherName: '',
                    publicationUrl: '',
                    publicationProofUrl: ''
                };
            },
            openCheckVue() {
                // 使用路由导航进行页面跳转
                this.$router.push({path: '/student/CheckProgress'});
            },
            // 添加期刊提交记录
            async doAddPublish() {
                // 表单验证
                this.cannotAddPublish = true
                if (this.publish.indicatorId !== null && this.publish.indicatorId !== "") {
                    this.cannotAddPublish = false
                    this.$refs["publicationForm"].validate(async valid => {
                        if (valid) {
                            const year = this.publish.year;
                            const indicatorId = this.publish.indicatorId;
                            const duplicates = this.publishToDatabase.check_duplicates;

                            const length = duplicates.indicatorId.length;
                            for (let i = 0; i < length; i++) {
                                if (year == duplicates.year[i] && indicatorId == duplicates.indicatorId[i]) {
                                    this.$message.error("这个期刊已经在数据库中存在了！");
                                    return;
                                }
                            }

                            this.publishToDatabase = {
                                publicationAbbr: this.publish.publicationAbbr,
                                publicationName: this.publish.publicationName,
                                publisherName: this.publish.publisherName,
                                publicationUrl: this.publish.publicationUrl,
                                publicationId: this.publish.publicationId,
                                indicatorId,
                                year,
                                studentId: this.user.id,
                                date: this.dateFormatFunc(new Date()),
                                state: "commit",
                                publicationProofUrl: this.publish.publicationProofUrl
                            };
                            const url = `/publicationSubmission/insert/`;
                            try {
                                const resp = await this.postRequest1(url, this.publishToDatabase);
                                if (resp && resp.msg === "200") {
                                    this.$message.success("成功发送！");
                                    this.dialogVisible_publication = false
                                }
                            } catch (error) {
                                this.$message.error(error);
                            }
                        } else {
                            // 表单验证不通过，给出错误提示
                            this.$message.error("表单填写不完整或不正确");
                        }
                    });
                }
            },
            handleDeletePublication() {//删除选择的文件
                var file = {
                    filepath: this.publish.publicationProofUrl
                }
                this.postRequest1("/publicationSubmission/deleteFile", file).then(
                    (res) => {
                        this.$message.success('删除成功！')
                    }, () => {
                        this.$message.success('删除失败！')
                    }
                )
            },
            handleChangeFilesPublication(file) {//文件列表数量改变
                this.filesPublication = []
                var attachmentType = [
                    "doc", "docx", "pdf", "jpg", "png", "jpeg", "rar", "zip"]
                var type = file.name.split('.')
                if (file.size > 10 * 1024 * 1024) {
                    this.$message.error('上传文件大小不能超过10MB!');
                    return
                }
                if (attachmentType.indexOf(type[type.length - 1].toLowerCase()) === -1) {
                    this.$message.error("不支持上传该类型的附件")
                    return
                }
                var formData = new FormData();
                this.filesPublication.push(file);
                formData.append("file", this.filesPublication[0].raw)
                axios.post("/publicationSubmission/upload", formData, {
                    headers: {
                        'token': this.user ? this.user.token : ''
                    }
                }).then(
                    (response) => {
                        this.$message({
                            message: '上传成功！'
                        })
                        this.cannotAddPublish = false
                        //获取文件路径
                        this.publish.publicationProofUrl = response.data
                    }, () => {
                    }
                )
            },
            openUpdateDialog() {
                if (this.inputDisabled) {
                    this.publish.publicationName = this.publication.publicationName
                    this.publish.publicationAbbr = this.publication.publicationAbbr
                    this.publish.publisherName = this.publication.publisherName
                    this.publish.publicationUrl = this.publication.publicationUrl
                    this.publish.publicationId = this.publication.publicationId
                }
                // 这里需要将之前获取的publication存储在this.publish中
                this.title_publication = "修改期刊"
                this.inputDisabled = true
                this.dialogVisible = false
                this.dialogVisible_publication = true
            },
            openAddDialog() {
                this.emptyPublish()
                this.inputDisabled = false
                this.title_publication = "添加期刊"
                this.dialogVisible = false
                this.dialogVisible_publication = true
            },
            // 显示添加项目对话框
            showAddEmpView() {

                this.dialogVisible = true;
                this.title = '添加项目';
                this.currentProjectCopy = {
                    name: '',
                    applicationTime: '',
                    category: '',
                    participants: '',
                    url: ''
                };
                this.rules = {
                    name: [
                        {required: true, message: '请输入项目名称', trigger: 'blur'}
                    ],
                    applicationTime: [
                        {required: true, message: '请选择申报时间', trigger: 'change'}
                    ],
                    category: [
                        {required: true, message: '请选择项目类别', trigger: 'change'}
                    ],
                    participants: [
                        {required: true, message: '请输入参与人', trigger: 'blur'}
                    ],
                    url: [
                        {required: true, message: '请上传证明材料', trigger: 'change'}
                    ]
                };
            },
            initEmps() {
                this.loading = true;
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
                params.teacherId = JSON.parse(localStorage.getItem("user")).id
                this.postRequest('/project/data/basic/teacherOrAdmin',params).then((resp) => {
                    this.loading = false;
                    if (resp) {
                        this.emps = resp.data;
                        console.log(this.emps)
                    }
                }).catch(() => {
                    this.loading = false;
                    this.$message.error('获取项目数据失败');
                });
                // let url = "/project/data/basic/studentID?studentID=" + this.user.id
                // this.getRequest(url).then((resp) => {
                //   this.loading = false;
                //   if (resp) {
                //     this.emps = resp.data;
                //   }
                // });
            },
            cancelAddStandard() {
                this.dialogVisible_publication_Standard = false;
            },

            handleNodeClick(data, node) {
                if (data.children.length == 0) {
                    this.publish.indicatorId = data.id
                    this.buttonText = data.label
                    this.showTree = false;
                    this.dialogVisible_publication = true;
                }
            },
            handleNodeClickPatent(data, node) {
                if (data.children.length == 0) {
                    this.indicatorBtn = data.label;
                    this.patentStatusList = [];
                    this.disabledGrantedStatusSelected = false;
                    this.currentSelectedIndicator = data;
                    this.currentPatentCopy.indicatorId = data.id;
                    //根据选择指标点的level，决定授权状态的可选列表有哪些
                    this.judgeGrantedStatusSelected(data);
                    if (!this.isAuthorIncludeSelf) {
                        this.patentPoint = 0;
                        this.zeroPointReason = '参与人未包含自己'
                    } else {
                        this.patentPoint = data.score;
                        this.zeroPointReason = '';
                    }
                    this.showTreeDialog = false;
                }
            },
            initTrees(type) {
                this.getRequest("/indicator/getAllByType?type=" + type).then(resp => {
                    this.showTreeDialog = true;
                    this.defaultExpandedKeys = [];
                    if (resp) {
                        this.indicatorData = resp.obj[1];
                        if (this.indicatorData.length > 0)
                            if (this.indicatorData[0].children.length > 0) {
                                this.defaultExpandedKeys.push(this.indicatorData[0].children[0].id);
                            } else this.defaultExpandedKeys.push(this.indicatorData[0].id);
                    }
                });
            },
            addStandard() {//项目提交确认
                const params = {};
                params.id = this.currentStandardCopy.id;
                params.name = this.currentStandardCopy.name;
                params.url = this.urlFile;
                params.rank = this.currentStandardCopy.rank;
                params.total = this.currentStandardCopy.total;
                params.author = this.currentStandardCopy.author;
                params.indicatorId = this.currentStandardCopy.indicatorId;
                params.author = this.currentStandardCopy.author;
                params.date = this.currentStandardCopy.date;
                params.point = this.standardPoint;
                params.state = "commit";
                params.studentId = this.user.id;
                if (JSON.stringify(this.currentSelectedIndicator) == '{}') {
                    this.$message.error('请选择指标点!');
                    return;
                }
                if (params.url == '' || params.url == null) {
                    this.$message.error('请上传证明材料！')
                    return;
                }
                if (params.url.indexOf("\\") >= 0) {
                    params.url = params.url.replaceAll("\\", "/")
                }
                if (!this.isAuthorIncludeSelf) {
                    this.$message.error("您的姓名【 " + this.user.name + " 】不在列表中！请确认作者列表中您的姓名为【" + this.user.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
                    return;
                }
                if (this.currentStandardCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
                    this.editStandard(params);
                } else {
                    this.$refs["currentStandardCopy"].validate((valid) => {
                        if (valid) {
                            params.studentId = this.user.id
                            this.postRequest1("/standard/basic/add", params).then(
                                (resp) => {
                                    if (resp) {
                                        this.$message.success('添加成功！')
                                        this.dialogVisible = false;
                                        this.doAddOper("commit", resp.data);
                                    }
                                }
                            );
                        }
                    });
                }
            },

            editCompetition(params) {
                params.studentId = this.user.id;
                this.$refs["currentCompetitionCopy"].validate((valid) => {
                    if (valid) {
                        params.id = this.currentCompetitionCopy.id;
                        this.postRequest1("/competition/basic/edit", params).then(
                            (resp) => {
                                if (resp) {
                                    this.dialogVisible_publication_AcademicCompetition = false;
                                    this.$message.success('编辑成功！')
                                    this.doAddOper("commit", this.currentCompetitionCopy.id);
                                }
                            }
                        );
                    }
                });
            },

            addCompetition() {//学科竞赛提交确认
                const params = {};
                params.name = this.currentCompetitionCopy.name;
                params.url = this.urlFile;
                params.rank = this.currentCompetitionCopy.rank;
                params.total = this.currentCompetitionCopy.total;
                params.author = this.currentCompetitionCopy.author;
                params.date = this.currentCompetitionCopy.date;
                params.point = this.competitionPoint;
                params.competitionTypeId = this.selectCompetitionType.id;
                params.competitionLevel = '';
                params.indicatorId = this.selectedIndicator.id;
                params.state = "commit";
                params.studentId = this.user.id;
                if (JSON.stringify(this.selectedIndicator) === '{}') {
                    this.$message.error('请选择指标点！');
                    return;
                }
                if (JSON.stringify(this.selectCompetitionType) == '{}' || this.selectCompetitionType == '') {
                    this.$message.error('请选择竞赛类别！')
                    return;
                }
                if (params.url == '' || params.url == null) {
                    this.$message.error('请上传证明材料！')
                    return
                }
                if (params.url.indexOf("\\") >= 0) {
                    params.url = params.url.replaceAll("\\", "/")
                }
                if (!this.isAuthorIncludeSelf) {
                    this.$message.error("您的姓名【 " + this.user.name + " 】不在列表中！请确认作者列表中您的姓名为【" + this.user.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
                    return;
                }

                if (this.currentCompetitionCopy.id) {
                    this.editCompetition(params);
                } else {
                    this.$refs["currentCompetitionCopy"].validate((valid) => {
                        if (valid) {
                            params.studentId = this.user.id;
                            this.postRequest1("/competition/basic/add", params).then(
                                (resp) => {
                                    if (resp) {
                                        this.$message.success('添加成功！')
                                        this.dialogVisible = false;
                                        this.doAddOper("commit", resp.data);
                                    }
                                }
                            );
                        }
                    });
                }
            },
            debouncedecisionSearchType(data) {
                if (this.currentDecisionCopy.date != null && this.currentDecisionCopy.date != '' && data != null && data != '') {
                    this.getRequest('/decision/basic/getIndicatorByYearAndType?year=' + this.currentDecisionCopy.date.split('-')[0] + '&indicatorId=' + this.currentIndicator.id).then(response => {
                        if (response) {
                            this.selectDecisionTypeList = response.data;
                            this.searchTypeLoading = false;
                        }
                    })
                }
            },
            cancelAddProduct() {
                this.dialogVisible_publication_Product = false;
            },
            canceldecisionAdd() {
                this.dialogVisible_publication_Decision = false;
            },
            selectDecisionTypeMethod(data) {
                this.searchTypeLoading = true;
                this.debouncedecisionSearchType(data);
            },
            changeDecisionStartDate(data) { //选择年月和指标点后才可以输入选择类别
                if (data) {
                    this.disabledSelectDecisionType = false;
                } else {
                    this.disabledSelectDecisionType = true;
                }
            },
            editPaper(params) {
                this.$refs["currentEmp"].validate(async (valid) => {
                    if (valid) {
                        params.ID = this.currentEmp.id;
                        this.postRequest1("/paper/basic/edit", params).then((resp) => {
                            if (resp) {
                                this.dialogVisible_publication_Paper = false;
                                this.doAddOper("commit", this.currentEmp.id)
                            }
                        });
                    }
                });
            },
            async doAddOper(state,remark,patentID,type) {
                this.oper.state = state;
                this.oper.remark = remark;
                this.oper.prodId = patentID;
                this.oper.prodType = type;
                this.oper.time = this.dateFormatFunc(new Date());
                this.oper.operatorRole = "teacher";
                if(this.oper.state == "tea_pass" || this.oper.state == 'adm_pass'){
                    this.oper.operationName = "审核通过"
                } else if (this.oper.state =="tea_reject" || this.oper.state == 'adm_reject'){
                    this.oper.operationName = "审核驳回"
                }
                await this.postRequest1("/oper/basic/add", this.oper);
                // await this.searchPatentListByCondicitions(this.currentPage, this.pageSize)
                await   this.fetchProjects()
                // 刷新列表数据
                this.initEmps();
            },
            doAddEmp() {//确定添加论文
                const params = {};
                params.name = this.currentEmp.name;
                params.url = this.urlFile;
                params.rank = this.currentEmp.rank;
                params.total = this.currentEmp.total;
                params.author = this.currentEmp.author;
                params.year = this.currentEmp.year;
                params.month = this.currentEmp.month;
                params.point = this.paperPoint;
                params.state = "commit";
                params.studentID = this.user.id
                params.pubPage = `${this.currentEmp.startPage}-${this.currentEmp.endPage}`;
                if (this.currentEmp.startPage == '' || this.currentEmp.startPage == null || this.currentEmp.endPage == '' || this.currentEmp.endPage == null) {
                    this.$message.warning('请填写正确页码！')
                    return
                }
                if (parseInt(this.currentEmp.startPage) > parseInt(this.currentEmp.endPage)) {
                    this.$message.warning('请填写正确页码！')
                    return
                }
                if (params.url == '' || params.url == null) {
                    this.$message.error('请上传证明材料！')
                    return
                }
                if (params.url.indexOf("\\") >= 0) {
                    params.url = params.url.replaceAll("\\", "/")
                }
                if (!this.isAuthorIncludeSelf) {
                    this.$message.error("您的姓名【 " + this.user.name + " 】不在列表中！请确认作者列表中您的姓名为【" + this.user.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
                    return;
                }
                if (this.publicationName == '' || this.publicationName == null) {
                    this.$message.error('请输入期刊名称！')
                    return
                }
                if (params.publicationID < 0) return;
                params.publicationID = this.publicationId;
                if (this.currentEmp.id) {//emptyEmp中没有将id设置为空 所以可以判断
                    this.editPaper(params);
                } else {
                    this.$refs["currentEmp"].validate(async (valid) => {
                        if (valid) {
                            this.postRequest1("/paper/basic/add", params).then(
                                (resp) => {
                                    if (resp) {
                                        this.dialogVisible_publication_Paper = false;
                                        this.doAddOper("commit", resp.data)
                                    }
                                }
                            );
                        }
                    });
                }
            },

            cancelResearchAdd() {
                this.dialogVisible_publication_ResearchProject = false;
            },

            changeCompetitionStartDate(data) {
                if (data) {
                    this.disabledSelectCompetitionType = false;
                } else {
                    this.disabledSelectCompetitionType = true;
                }
            },
            cancelAddProject() {
                this.dialogVisible_publication_HorizontalResearchProject = false;
            },
            changeProjectStartDate(data) {
                if (data) {
                    this.disabledSelectProjectType = false;
                } else {
                    this.disabledSelectProjectType = true;
                }
            },
            selectOption(data) {
                if (data) {
                    this.getRequest('/project/basic/getIndicatorScore?id=' + data.indicatorId).then(response => {
                        if (response) {
                            this.projectPoint = response.data.score;
                            this.currentIndicator = response.data;
                            this.judgeMember(); //若填写过作者列表，需要重新判断
                        } else {
                            this.projectPoint = 0;
                            this.zeroPointReason = '';
                            this.currentIndicator = {};
                        }
                    })
                }
            },
            editAwardPatent(params) {
                params.studentId = this.user.id
                this.$refs["currentPatentCopy"].validate((valid) => {
                    if (valid) {
                        this.postRequest1("/patent/basic/edit", params).then(
                            (resp) => {
                                if (resp) {
                                    this.dialogVisible_publication_Patent = false;
                                    this.doAddOper("commit", this.currentPatentCopy.id);
                                    this.$message.success('编辑成功！')
                                }
                            }
                        );
                    }
                });
            },
            addAwardPatent() {//项目提交确认
                const params = {};
                params.id = this.currentPatentCopy.id;
                params.name = this.currentPatentCopy.name;
                params.url = this.urlFile;
                params.rank = this.currentPatentCopy.rank;
                params.total = this.currentPatentCopy.total;
                params.author = this.currentPatentCopy.author;
                params.grantedStatus = this.currentPatentCopy.grantedStatus;
                params.indicatorId = this.currentPatentCopy.indicatorId;
                params.author = this.currentPatentCopy.author;
                params.date = this.currentPatentCopy.date;
                params.point = this.patentPoint;
                params.state = "commit";
                params.studentId = this.user.id
                if (params.url == '' || params.url == null) {
                    this.$message.error('请上传证明材料！')
                    return
                }
                if (params.url.indexOf("\\") >= 0) {
                    params.url = params.url.replaceAll("\\", "/")
                }
                if (!this.isAuthorIncludeSelf) {
                    this.$message.error("您的姓名【 " + this.user.name + " 】不在列表中！请确认作者列表中您的姓名为【" + this.user.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
                    return;
                }
                if (this.currentPatentCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
                    this.editAwardPatent(params);
                } else {
                    this.$refs["currentPatentCopy"].validate((valid) => {
                        if (valid) {
                            params.studentId = this.user.id
                            this.postRequest1("/patent/basic/add", params).then(
                                (resp) => {
                                    if (resp) {
                                        this.$message.success('添加成功！')
                                        this.dialogVisible = false;
                                        this.doAddOper("commit", resp.data);
                                    }
                                }
                            );
                        }
                    });
                }
            },
            editResearchAward(params) {
                this.$refs["currentAwardCopy"].validate((valid) => {
                    if (valid) {
                        params.id = this.currentAwardCopy.id;
                        params.studentId = this.user.id
                        params.awardTypeId = this.currentAwardCopy.awardType.id;
                        this.postRequest1("/award/basic/edit", params).then(
                            (resp) => {
                                if (resp) {
                                    this.dialogVisible_publication_ResearchAward = false;
                                    this.$message.success('编辑成功！')
                                    this.doAddOper("commit", this.currentAwardCopy.id);
                                }
                            }
                        );
                    }
                });
            },
            addResearchAward() {//项目提交确认
                const params = {};
                params.name = this.currentAwardCopy.name;
                params.url = this.urlFile;
                params.rank = this.currentAwardCopy.rank;
                params.total = this.currentAwardCopy.total;
                params.author = this.currentAwardCopy.author;
                params.date = this.currentAwardCopy.date;
                params.point = this.awardPoint;
                params.awardLevel = this.currentAwardCopy.awardLevel;
                params.state = "commit";
                params.indicatorId = this.selectedIndicator.id;
                if (params.url == '' || params.url == null) {
                    this.$message.error('请上传证明材料！')
                    return
                }
                if (params.url.indexOf("\\") >= 0) {
                    params.url = params.url.replaceAll("\\", "/")
                }
                if (!this.isAuthorIncludeSelf) {
                    this.$message.error("您的姓名【 " + this.user.name + " 】不在列表中！请确认作者列表中您的姓名为【" + this.user.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
                    return;
                }
                if (this.currentAwardCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
                    this.editResearchAward(params);
                } else {
                    this.$refs["currentAwardCopy"].validate((valid) => {
                        if (valid) {
                            params.studentId = this.user.id;
                            params.awardTypeId = this.selectAwardType.id;
                            this.postRequest1("/award/basic/add", params).then(
                                (resp) => {
                                    if (resp) {
                                        this.$message.success('添加成功！')
                                        this.dialogVisible = false;
                                        this.doAddOper("commit", resp.data);
                                    }
                                }
                            );
                        }
                    });
                }
            },

            editMonograph(params) {
                params.studentId = this.user.id;
                this.$refs["currentMonographCopy"].validate((valid) => {
                    if (valid) {
                        params.id = this.currentMonographCopy.id;
                        this.postRequest1("/monograph/basic/edit", params).then(
                            (resp) => {
                                if (resp) {
                                    this.dialogVisible_publication_AcademicMonograph = false;
                                    this.$message.success('编辑成功！')
                                    this.doAddOper("commit", this.currentMonographCopy.id);
                                }
                            }
                        );
                    }
                });
            },

            addMonograph() {//专著或教材提交确认
                const params = {};
                params.name = this.currentMonographCopy.name;
                params.url = this.urlFile;
                params.rank = this.currentMonographCopy.rank;
                params.total = this.currentMonographCopy.total;
                params.author = this.currentMonographCopy.author;
                params.indicatorId = this.currentMonographCopy.indicatorId;
                params.date = this.currentMonographCopy.date;
                params.publisher = this.currentMonographCopy.publisher;
                params.isbn = this.currentMonographCopy.isbn;
                params.point = this.monographPoint;
                params.state = "commit";
                params.studentId = this.user.id;
                if (params.url == '' || params.url == null) {
                    this.$message.error('请上传证明材料！')
                    return
                }
                if (params.url.indexOf("\\") >= 0) {
                    params.url = params.url.replaceAll("\\", "/")
                }
                if (!this.isAuthorIncludeSelf) {
                    this.$message.error('请仔细检查作者列表！');
                    return;
                }
                if (this.currentMonographCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
                    this.editMonograph(params);
                } else {
                    this.$refs["currentMonographCopy"].validate((valid) => {
                        if (valid) {
                            params.studentId = this.user.id
                            this.postRequest1("/monograph/basic/add", params).then(
                                (resp) => {
                                    if (resp) {
                                        this.$message.success('添加成功！')
                                        this.dialogVisible = false;
                                        this.doAddOper("commit", resp.data);
                                    }
                                }
                            );
                        }
                    });
                }
            },


            // 格式化积分展示效果为x/y x为实际获得分数,y为论文积分
            formatPoint(row, column, cellValue) {
                const status = row.status;
                const y = cellValue;
                let x = 0;

                const pointtype = row.pointtype;
                if (pointtype===0&& status === 'adm_pass'){
                    x = y;
                }else if (pointtype===1){
                    x = y;
                }else if (pointtype===2){
                    x = 0;
                }



                return `${x}/${y}`;
            },
            throttleSearchType() {
                if (this.currentCompetitionCopy.date == null || this.currentCompetitionCopy.date == '') return;
                this.getRequest('/competition/basic/getIndicatorByYearAndType?year=' + this.currentCompetitionCopy.date.split('-')[0]).then(response => {
                    if (response) {
                        this.searchTypeLoading = false;
                        this.selectCompetitionTypeList = response.data;
                    }
                })
            },
            selectCompetitionTypeMethod() {
                this.searchTypeLoading = true;
                this.throttleSearchType();
            },

            selectProjectTypeMethod(data) {
                if (data == null || data == '') {
                    return;
                }
                this.searchTypeLoading = true;
                this.debounceSearch(data);
            },
            cancelAddPatent() {
                this.dialogVisible_publication_Patent = false;
            },
            cancellationAdd() {
                this.dialogVisible_publication_AcademicCompetition = false;
            },


            projectPoint() {

            },

            rowClass() {
                return 'background:#b3d8ff;color:black;font-size:13px;text-align:center'
            },

            handleExceed() {//超过限制数量
                this.$message.error(`只允许上传1个文件`);
            },
            handleChangeFiles(file, fileList) {//文件列表数量改变
                this.files = []
                var attachmentType = [
                    "doc", "docx", "pdf", "jpg", "png", "jpeg", "rar", "zip"]
                var type = file.name.split('.')
                if (file.size > 10 * 1024 * 1024) {
                    this.$message.error('上传文件大小不能超过10MB!');
                    return
                }
                if (attachmentType.indexOf(type[type.length - 1].toLowerCase()) === -1) {
                    this.$message.error("不支持上传该类型的附件")
                    return
                }
                var formData = new FormData();
                this.files.push(file);
                formData.append("file", this.files[0].raw)
                axios.post("/achievements/basic/upload", formData, {
                    headers: {
                        'token': this.user ? this.user.token : ''
                    }
                }).then(
                    (response) => {
                        this.$message({
                            message: '上传成功！'
                        })
                        //获取文件路径
                        this.urlFile = response.data
                    }, () => {
                    }
                )
            },

            handleDelete() {

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
                if (typeMap[data.category]) {
                    console.log(data.category + '显示详情:' + typeMap[data.category] + '--->' + dataMap[data.category])
                    this.getRequest(urlMap[data.category] + "/basic/getDtaById?id=" + data.id).then((resp) => {
                        this.loading = false;
                        if (resp) {
                            this[dataMap[data.category]] = resp.data;
                        }
                    });
                    this[typeMap[data.category]] = true;
                }
            },

            judgeGrantedStatusSelected(data) {
                if (data.level == null || data.level == '') {
                    this.patentStatusList = this.patentStatusListObject.slice(3);
                } else {
                    this.patentStatusListObject.map(item => {
                        if (item.name == data.level) {
                            if (item.value < 3) { //如果选中的指标点状态属于前三个
                                this.patentStatusList = this.patentStatusListObject.slice(3);
                                return;
                            } else { //如果选中的指标点状态属于后三个
                                this.patentStatusList = this.patentStatusListObject.slice(item.value);
                                return;
                            }
                        }
                    })
                }
            },
            changeAwardDate(data) {
                if (data) {
                    this.disabledSelectAwardType = false;
                } else {
                    this.disabledSelectAwardType = true;
                }
            },
            debounceSearchType() {
                if (this.currentAwardCopy.date == null || this.currentAwardCopy.date == '') return;
                if (this.currentAwardCopy.awardLevel == null || this.currentAwardCopy.awardLevel == '') return;
                this.searchTypeLoading = true;
                this.getRequest('/award/basic/getIndicatorByYearAndType?year=' + this.currentAwardCopy.date.split('-')[0] + '&type=' + this.currentAwardCopy.awardLevel).then(response => {
                    if (response) {
                        this.searchTypeLoading = false;
                        this.selectAwardTypeList = response.data;
                    }
                })
            },
            selectAwardTypeMethod() {
                this.debounceSearchType();
            },
            handleShowInfo(data) {
                this.title_show = "显示详情";
                console.log(data)
                this.currentProjectSummary = data
                this.showInfoMap(data)
                this.getRequest("/oper/basic/List?prodId=" + data.id + '&type=' + data.category).then((resp) => {
                    this.loading = false;
                    if (resp) {
                        this.operList = resp.obj
                    }
                });
                if(data.category==='项目开发')
                    return;
                this.isPdf = this.isImage = false; //初始化
                this.previewUrl = '';
                this.previewImageSrcList = [];
                this.isDataUrl(data)
                
            },
            // 显示项目详情对话框
            showInfo(data) {
                this.currentType = data.category;

                this.handleShowInfo(data);
            },
            // 文件下载
            previewMethod(type) {
                if (type == '1') {
                    this.previewFileMethod(this.emp).then(res => {
                        this.previewUrl = res;
                        if (this.isImage) {
                            this.previewImageSrcList = [res];
                            this.$refs.previewImage.showViewer = true;
                        }
                        if (this.isPdf) {
                            this.dialogPreviewPdfFile = true;
                        }
                    });
                }  else if (type == '2'){//学术论文
                    this.downloadFileMethod(this.emp);

                }else if (type == '3'){//授权专利
                    this.downloadFileMethod(this.currentPatent);
                } else if (type == '4'){//科研获奖
                    this.downloadFileMethod(this.currentAward);
                }
                else if (type == '5'){//学术专著和教材
                    this.downloadFileMethod(this.currentMonograph);
                }
                else if (type == '6'){//横向-纵向
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
            cancelAdd() {
                this.dialogVisible_publication_ResearchAward = false;
            },
            cancelMonographAdd() {
                this.dialogVisible_publication_AcademicMonograph = false;
            },
            //编辑数据
            showEditEmpView(data) {
                if (data.category === '学术论文') {
                    this.title = "编辑论文信息";
                    this.dialogVisible_publication_Paper = true
                    this.getRequest("/paper/basic/studentIDInfo?studentID=" + this.user.id + "&id=" + data.id).then((resp) => {
                        this.loading = false;
                        if (resp) {
                            this.currentEmp = resp.data;
                            this.files = [
                                {
                                    name: this.currentEmp.url.split('/').reverse()[0],
                                    url: this.currentEmp.url
                                }
                            ];
                            this.currentEmp.date = this.currentEmp.year + "-" + this.currentEmp.month;
                            this.currentEmp.startPage = this.$set(this.currentEmp, 'startPage', this.currentEmp.pubPage.split('-')[0])
                            this.currentEmp.endPage = this.$set(this.currentEmp, 'endPage', this.currentEmp.pubPage.split('-')[1])
                            this.paperPoint = resp.data.point;
                            this.disabledInput = false;
                            this.urlFile = this.currentEmp.url;
                            this.dialogVisible = true;
                            this.publicationName = this.currentEmp.pubName;
                            this.isInitEditDialog = true;
                            this.isAuthorIncludeSelf = true;
                            this.publicationId = resp.data.publication.id;
                        }
                    });
                } else if (data.category === '授权专利') {
                    this.dialogVisible_publication_Patent = true;
                    this.getRequest("/patent/basic//studentIDInfo?studentID=" + this.user.id + "&id=" + data.id).then((resp) => {
                        if (resp) {
                            this.dialogVisible = true;
                            this.title = "编辑专利信息";
                            this.disabledGrantedStatusSelected = false;
                            this.judgeGrantedStatusSelected(resp.data.indicator);
                            this.currentPatentCopy = JSON.parse(JSON.stringify(resp.data));
                            this.files = [
                                {
                                    name: this.currentPatentCopy.url.split('/').reverse()[0],
                                    url: this.currentPatentCopy.url
                                }
                            ];
                            this.indicatorBtn = resp.data.indicator.name;
                            this.patentPoint = resp.data.point;
                            this.zeroPointReason = '';
                            this.urlFile = this.currentPatentCopy.url;
                            this.isAuthorIncludeSelf = true;
                            this.addButtonState = true;
                        }
                    });
                } else if (data.category === '科研获奖') {
                    this.dialogVisible_publication_ResearchAward = true;
                    this.getRequest("/award/basic/studentIDInfo?studentID=" + this.user.id + "&id=" + data.id).then((resp) => {
                        if (resp) {
                            this.dialogVisible = true;
                            this.title = "编辑奖励信息";
                            this.currentAwardCopy = JSON.parse(JSON.stringify(resp.data));
                            this.isAuthorIncludeSelf = true;
                            this.disabledSelectAwardType = false;
                            this.selectedAwardLevel = false;
                            this.awardPoint = resp.data.point;
                            this.zeroPointReason = '';
                            const {id, name} = resp.data.awardType;
                            this.selectAwardType = name;
                            this.dialogVisible = true;
                            this.awardLimitRankN = resp.data.indicator.rankN;
                            this.files = [
                                {
                                    name: this.currentAwardCopy.url.split('/').reverse()[0],
                                    url: this.currentAwardCopy.url
                                }
                            ]
                            this.urlFile = this.currentAwardCopy.url;
                        }
                    });
                } else if (data.category === '学术专著和教材') {
                    this.dialogVisible_publication_AcademicMonograph = true;
                    this.title = "编辑专著或教材信息";
                    this.getRequest("/monograph/basic/studentIDInfo?studentID=" + this.user.id + "&id=" + data.id).then((resp) => {
                        if (resp) {
                            this.currentMonographCopy = JSON.parse(JSON.stringify(resp.data));
                            this.options = [];
                            this.monoLimitRankN = resp.data.indicator.rankN;
                            this.monographPoint = resp.data.point;
                            this.zeroPointReason = '';
                            this.isAuthorIncludeSelf = true;
                            this.addButtonState = true;
                            this.indicatorBtn = resp.data.indicator.name;
                            this.files = [
                                {
                                    name: this.currentMonographCopy.url.split('/').reverse()[0],
                                    url: this.currentMonographCopy.url
                                }
                            ]
                            this.urlFile = this.currentMonographCopy.url;
                        }
                    });
                } else if (data.category === '纵向科研项目') {
                    this.title = "编辑科研项目信息";
                    this.dialogVisible_publication_ResearchProject = true;
                    this.getRequest("/project/basic/studentIDInfo?studentID=" + this.user.id + "&id=" + data.id + "&type=0").then((resp) => {
                        if (resp) {
                            this.currentProjectCopy = JSON.parse(JSON.stringify(resp.data));
                            this.disabledSelectProjectType = false;
                            this.currentIndicator = resp.data.indicator;
                            this.files = [
                                {
                                    name: this.currentProjectCopy.url.split('/').reverse()[0],
                                    url: this.currentProjectCopy.url
                                }
                            ];
                            this.urlFile = this.currentProjectCopy.url;
                            this.selectProjectType = resp.data.projectType.name;
                            this.projectPoint = resp.data.point;
                            this.isAuthorIncludeSelf = true;
                        }
                    });
                } else if (data.category === '项目开发') {
                    this.dialogVisible_publication_HorizontalResearchProject = true;
                    this.title = "编辑项目信息";
                    this.getRequest("/project/basic/studentIDInfo?studentID=" + this.user.id + "&id=" + data.id + "&type=1").then((resp) => {
                        if (resp) {
                            this.currentSelectedIndicator = resp.data.indicator;
                            this.currentProjectCopy = JSON.parse(JSON.stringify(resp.data));
                            this.files = [
                                {
                                    name: this.currentProjectCopy.url.split('/').reverse()[0],
                                    url: this.currentProjectCopy.url
                                }
                            ];
                            this.indicatorBtn = resp.data.indicator.name;
                            this.projectPoint = resp.data.point;
                            this.zeroPointReason = '';
                            this.urlFile = this.currentProjectCopy.url;
                            this.isAuthorIncludeSelf = true;
                            this.addButtonState = true;
                        }
                    });
                } else if (data.category === '学科竞赛') {
                    this.title = "编辑学科竞赛信息";
                    this.dialogVisible_publication_AcademicCompetition = true;
                    this.getRequest("/competition/basic/studentIDInfo?studentID=" + this.user.id + "&id=" + data.id).then((resp) => {
                        if (resp) {
                            this.competitionLimitRankN = resp.data.indicator.rankN;
                            this.selectedIndicator = resp.data.indicator;
                            this.indicatorBtn = this.selectedIndicator.name;
                            this.currentCompetitionCopy = JSON.parse(JSON.stringify(resp.data));
                            this.disabledSelectCompetitionType = false;
                            this.files = [
                                {
                                    name: this.currentCompetitionCopy.url.split('/').reverse()[0],
                                    url: this.currentCompetitionCopy.url
                                }
                            ];
                            this.urlFile = this.currentCompetitionCopy.url;
                            this.selectCompetitionType = resp.data.competitionType.name;
                            this.competitionPoint = resp.data.point;
                            this.zeroPointReason = '';
                            this.isAuthorIncludeSelf = true;
                        }
                    });
                } else if (data.category === '决策咨询') {
                    this.title = "编辑决策信息";
                    this.dialogVisible_publication_Decision = true;
                    this.getRequest("/decision/basic/studentIDInfo?studentID=" + this.user.id + "&id=" + data.id).then((resp) => {
                        if (resp) {
                            this.currentIndicator = resp.data.indicator;
                            this.indicatorBtn = this.currentIndicator.name;
                            this.currentDecisionCopy = JSON.parse(JSON.stringify(resp.data));
                            this.isAuthorIncludeSelf = true;
                            this.disabledSelectDecisionType = false;
                            this.decisionPoint = resp.data.point;
                            const {id, name} = resp.data.decisionType;
                            this.selectDecisionType = name;
                            this.files = [
                                {
                                    name: this.currentDecisionCopy.url.split('/').reverse()[0],
                                    url: this.currentDecisionCopy.url
                                }
                            ]
                            this.urlFile = this.currentDecisionCopy.url;
                        }
                    });
                } else if (data.category === '撰写项目文档') {
                    this.dialogVisible_publication_Product = true;
                    this.title = "编辑项目文档信息";
                    this.getRequest("/product/basic/studentIDInfo?studentID=" + this.user.id + "&id=" + data.id).then((resp) => {
                        if (resp) {
                            this.currentSelectedIndicator = resp.data.indicator;
                            this.currentProductCopy = JSON.parse(JSON.stringify(resp.data));
                            this.files = [
                                {
                                    name: this.currentProductCopy.url.split('/').reverse()[0],
                                    url: this.currentProductCopy.url
                                }
                            ];
                            this.indicatorBtn = resp.data.indicator.name;
                            this.productPoint = resp.data.point;
                            this.zeroPointReason = '';
                            this.urlFile = this.currentProductCopy.url;
                            this.isAuthorIncludeSelf = true;
                            this.addButtonState = true;
                        }
                    });
                } else if (data.category === '制定标准') {
                    this.dialogVisible_publication_Standard = true;
                    this.title = "编辑标准信息";
                    this.getRequest("/standard/basic/studentIDInfo?studentID=" + this.user.id + "&id=" + data.id).then((resp) => {
                        if (resp) {
                            this.currentStandardCopy = JSON.parse(JSON.stringify(resp.data));
                            this.files = [
                                {
                                    name: this.currentStandardCopy.url.split('/').reverse()[0],
                                    url: this.currentStandardCopy.url
                                }
                            ];
                            this.indicatorBtn = resp.data.indicator.name;
                            this.currentSelectedIndicator = resp.data.indicator;
                            this.standardPoint = resp.data.point;
                            this.zeroPointReason = '';
                            this.urlFile = this.currentStandardCopy.url;
                            this.isAuthorIncludeSelf = true;
                        }
                    });
                }
            },

            editDecision(params) {
                this.$refs["currentDecisionCopy"].validate((valid) => {
                    if (valid) {
                        params.id = this.currentDecisionCopy.id;
                        params.decisionTypeId = this.currentDecisionCopy.decisionType.id;
                        params.studentId = this.user.id;
                        this.postRequest1("/decision/basic/edit", params).then(
                            (resp) => {
                                if (resp) {
                                    this.dialogVisible_publication_Decision = false;
                                    this.$message.success('编辑成功！')
                                    this.doAddOper("commit", this.currentDecisionCopy.id);
                                }
                            }
                        );
                    }
                });
            },
            addDecision() {//项目提交确认
                const params = {};
                params.name = this.currentDecisionCopy.name;
                params.url = this.urlFile;
                params.rank = this.currentDecisionCopy.rank;
                params.total = this.currentDecisionCopy.total;
                params.author = this.currentDecisionCopy.author;
                params.date = this.currentDecisionCopy.date;
                params.point = this.decisionPoint;
                params.decisionLevel = this.currentDecisionCopy.decisionLevel;
                params.state = "commit";
                params.indicatorId = this.currentIndicator.id;
                if (JSON.stringify(this.currentIndicator) === '{}') {
                    this.$message.error('请选择指标点！');
                    return;
                }
                if (JSON.stringify(this.selectDecisionType) == '{}' || this.selectDecisionType == '') {
                    this.$message.error('请选择决策类别！')
                    return;
                }
                if (params.url == '' || params.url == null) {
                    this.$message.error('请上传证明材料！')
                    return
                }
                if (params.url.indexOf("\\") >= 0) {
                    params.url = params.url.replaceAll("\\", "/")
                }
                if (!this.isAuthorIncludeSelf) {
                    this.$message.error("您的姓名【 " + this.user.name + " 】不在列表中！请确认作者列表中您的姓名为【" + this.user.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
                    return;
                }
                if (this.currentDecisionCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
                    this.editDecision(params);
                } else {
                    this.$refs["currentDecisionCopy"].validate((valid) => {
                        if (valid) {
                            params.studentId = this.user.id;
                            params.decisionTypeId = this.selectDecisionType.id;
                            this.postRequest1("/decision/basic/add", params).then(
                                (resp) => {
                                    if (resp) {
                                        this.$message.success('添加成功！')
                                        this.dialogVisible = false;
                                        this.doAddOper("commit", resp.data);
                                    }
                                }
                            );
                        }
                    });
                }
            },
            editAward(params) {
                params.studentId = this.user.id;
                this.$refs["currentProductCopy"].validate((valid) => {
                    if (valid) {
                        this.postRequest1("/product/basic/edit", params).then(
                            (resp) => {
                                if (resp) {
                                    this.dialogVisible_publication_Product = false;
                                    this.doAddOper("commit", this.currentProductCopy.id);
                                    this.$message.success('编辑成功！')
                                }
                            }
                        );
                    }
                });
            },
            addAward() {
                const params = {};
                params.id = this.currentProductCopy.id;
                params.name = this.currentProductCopy.name;
                params.url = this.urlFile;
                params.rank = this.currentProductCopy.rank;
                params.total = this.currentProductCopy.total;
                params.author = this.currentProductCopy.author;
                params.indicatorId = this.currentProductCopy.indicatorId;
                console.log(params.indicatorId)
                params.author = this.currentProductCopy.author;
                params.date = this.currentProductCopy.date;
                params.point = this.productPoint;
                params.state = "commit";
                params.studentId = this.user.id;
                if (params.url == '' || params.url == null) {
                    this.$message.error('请上传证明材料！')
                    return
                }
                if (params.url.indexOf("\\") >= 0) {
                    params.url = params.url.replaceAll("\\", "/")
                }
                if (!params.indicatorId) {
                    this.$message.error('请选择指标点！')
                    return;
                }
                if (!this.isAuthorIncludeSelf) {
                    this.$message.error("您的姓名【 " + this.user.name + " 】不在列表中！请确认作者列表中您的姓名为【" + this.user.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
                    return;
                }
                if (this.currentProductCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
                    this.editAward(params);
                } else {
                    this.$refs["currentProductCopy"].validate((valid) => {
                        if (valid) {
                            params.studentId = this.user.id
                            this.postRequest1("/product/basic/add", params).then(
                                (resp) => {
                                    if (resp) {
                                        this.$message.success('添加成功！')
                                        this.dialogVisible = false;
                                        this.doAddOper("commit", resp.data);
                                    }
                                }
                            );
                        }
                    });
                }
            },
            editProject(params) {
                this.$refs["currentProjectCopy"].validate((valid) => {
                    if (valid) {
                        params.id = this.currentProjectCopy.id;
                        params.studentId = this.user.id
                        if (JSON.stringify(this.selectProjectType) == '{}' || this.selectProjectType == '') {
                            this.$message.error('请选择项目类别！')
                            return;
                        }
                        if (!this.isAuthorIncludeSelf) {
                            this.$message.error("您的姓名【 " + this.user.name + " 】不在列表中！请确认作者列表中您的姓名为【" + this.user.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
                            return;
                        }
                        this.postRequest1("/project/basic/edit", params).then(
                            (resp) => {
                                if (resp) {
                                    this.dialogVisible_publication_ResearchProject = false;
                                    this.$message.success('编辑成功！')
                                    this.doAddOper("commit", this.currentProjectCopy.id);
                                }
                            }
                        );
                    }
                });
            },
            addProject() {//科研项目提交确认
                const params = {};
                params.name = this.currentProjectCopy.name;
                params.url = this.urlFile;
                params.rank = this.currentProjectCopy.rank;
                params.total = this.currentProjectCopy.total;
                params.author = this.currentProjectCopy.author;
                params.startDate = this.currentProjectCopy.startDate;
                params.endDate = this.currentProjectCopy.endDate;
                params.point = this.projectPoint;
                params.projectTypeId = this.selectProjectType.id;
                params.state = "commit";
                if (params.url == '' || params.url == null) {
                    this.$message.error('请上传证明材料！')
                    return
                }
                if (params.url.indexOf("\\") >= 0) {
                    params.url = params.url.replaceAll("\\", "/")
                }
                if (this.currentProjectCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
                    this.editProject(params);
                } else {
                    this.$refs["currentProjectCopy"].validate((valid) => {
                        if (valid) {
                            params.indicatorId = null;
                            if (JSON.parse(JSON.stringify(this.selectProjectType)) == '{}' || this.selectProjectType == '') {
                                this.$message.error('请选择项目类别！')
                                return;
                            }
                            params.studentId = this.user.id;
                            if (!this.isAuthorIncludeSelf) {
                                this.$message.error("您的姓名【 " + this.user.name + " 】不在列表中！请确认作者列表中您的姓名为【" + this.user.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
                                return;
                            }
                            this.postRequest1("/project/basic/add", params).then(
                                (resp) => {
                                    if (resp) {
                                        this.$message.success('添加成功！')
                                        this.dialogVisible = false;
                                        this.doAddOper("commit", resp.data);
                                    }
                                }
                            );
                        }
                    });
                }
            },
            editStandard(params) {
                params.studentId = this.user.id;
                this.$refs["currentStandardCopy"].validate((valid) => {
                    if (valid) {
                        this.postRequest1("/standard/basic/edit", params).then(
                            (resp) => {
                                if (resp) {
                                    this.dialogVisible_publication_Standard = false;
                                    this.doAddOper("commit", this.currentStandardCopy.id);
                                    this.$message.success('编辑成功！')
                                }
                            }
                        );
                    }
                });
            },
            editHorizontal(params) {
                this.$refs["currentProjectCopy"].validate((valid) => {
                    if (valid) {
                        params.id = this.currentProjectCopy.id;
                        this.postRequest1("/project/basic/edit", params).then(
                            (resp) => {
                                if (resp) {
                                    this.dialogVisible_publication_HorizontalResearchProject = false;
                                    this.doAddOper("commit", this.currentProjectCopy.id);
                                    this.$message.success('编辑成功！')
                                }
                            }
                        );
                    }
                });
            },
            addHorizontal() {//项目提交确认
                const params = {};
                params.name = this.currentProjectCopy.name;
                params.url = this.urlFile;
                params.rank = this.currentProjectCopy.rank;
                params.total = this.currentProjectCopy.total;
                params.author = this.currentProjectCopy.author;
                params.indicatorId = this.currentProjectCopy.indicatorId;
                params.author = this.currentProjectCopy.author;
                params.startDate = this.currentProjectCopy.startDate;
                params.point = this.projectPoint;
                params.state = "commit";
                params.studentId = this.user.id;
                if (params.url == '' || params.url == null) {
                    this.$message.error('请上传证明材料！')
                    return
                }
                if (params.url.indexOf("\\") >= 0) {
                    params.url = params.url.replaceAll("\\", "/")
                }
                if (!params.indicatorId) {
                    this.$message.error('请选择指标点！')
                    return;
                }
                if (!this.isAuthorIncludeSelf) {
                    this.$message.error("您的姓名【 " + this.user.name + " 】不在列表中！请确认作者列表中您的姓名为【" + this.user.name + " 】，注意拼写要完全正确。多个人员之间用分号分割");
                    return;
                }
                if (this.currentProjectCopy.id) {//emptyEmp中没有将id设置为空 所以可以判断
                    this.editHorizontal(params);
                } else {
                    this.$refs["currentProjectCopy"].validate((valid) => {
                        if (valid) {
                            params.projectTypeId = null;
                            this.postRequest1("/project/basic/add", params).then(
                                (resp) => {
                                    if (resp) {
                                        this.$message.success('添加成功！')
                                        this.dialogVisible = false;
                                        this.doAddOper("commit", resp.data);
                                    }
                                }
                            );
                        }
                    });
                }
            },
            // 删除数据
            deleteEmp(data) {
                if (data.category === '学术论文') {
                    this.$confirm("此操作将永久删除【" + data.name + "】, 是否继续?",).then(() => {
                        Promise.all([this.deletePaperEmpMethod(data), this.deletePaperOperationList(data)]).then(res => {
                            this.$message.success('删除成功!');
                            this.initEmps();
                        }).catch(() => {
                            this.$message.error('删除失败!');
                        })
                    })
                } else if (data.category === '授权专利') {
                    this.$confirm("此操作将永久删除【" + data.name + "】, 是否继续?").then(() => {
                        Promise.all([this.deletePatentEmpMethod(data), this.deletePatentOperationList(data)]).then(res => {
                            this.$message.success('删除成功!');
                            this.initEmps();
                        }).catch(() => {
                            this.$message.error('删除失败!');
                        })
                    })
                } else if (data.category === '科研获奖') {
                    this.$confirm("此操作将永久删除【" + data.name + "】, 是否继续?",).then(() => {
                        Promise.all([this.deleteAwardEmpMethod(data), this.deleteAwardOperationList(data)]).then(res => {
                            this.$message.success('删除成功!');
                            this.initEmps();
                        }).catch(() => {
                            this.$message.error('删除失败!');
                        })
                    })
                } else if (data.category === '学术专著和教材') {
                    this.$confirm("此操作将永久删除【" + data.name + "】, 是否继续?").then(() => {
                        Promise.all([this.deleteMonographEmpMethod(data), this.deleteMonographOperationList(data)]).then(res => {
                            this.$message.success('删除成功!');
                            this.initEmps();
                        }).catch((e) => {
                            console.log(e);
                            this.$message.error('删除失败!');
                        })
                    })
                } else if (data.category === '纵向科研项目') {
                    this.$confirm("此操作将永久删除【" + data.name + "】, 是否继续?").then(() => {
                        Promise.all([this.deleteProjectEmpMethod(data), this.deleteProjectOperationList(data)]).then(res => {
                            this.$message.success('删除成功!');
                            this.initEmps();
                        }).catch(() => {
                            this.$message.error('删除失败!');
                        })
                    })
                } else if (data.category === '项目开发') {
                    this.$confirm("此操作将永久删除【" + data.name + "】, 是否继续?").then(() => {
                        Promise.all([this.deleteHorizontalProjectEmpMethod(data), this.deleteHorizontalProjectOperationList(data)]).then(res => {
                            this.$message.success('删除成功!');
                            this.initEmps();
                        }).catch(() => {
                            this.$message.error('删除失败!');
                        })
                    })
                } else if (data.category === '学科竞赛') {
                    this.$confirm("此操作将永久删除【" + data.name + "】, 是否继续?").then(() => {
                        Promise.all([this.deleteCompetitionEmpMethod(data), this.deleteCompetitionOperationList(data)]).then(res => {
                            this.$message.success('删除成功!');
                            this.initCompetitionsList();
                        }).catch(() => {
                            this.$message.error('删除失败!');
                        })
                    })
                } else if (data.category === '决策咨询') {
                    this.$confirm("此操作将永久删除【" + data.name + "】, 是否继续?",).then(() => {
                        Promise.all([this.deleteDecisionEmpMethod(data), this.deleteDecisionOperationList(data)]).then(res => {
                            this.$message.success('删除成功!');
                            this.initDecisionsList();
                        }).catch(() => {
                            this.$message.error('删除失败!');
                        })
                    })
                } else if (data.category === '撰写项目文档') {
                    this.$confirm("此操作将永久删除【" + data.name + "】, 是否继续?").then(() => {
                        Promise.all([this.deleteProductEmpMethod(data), this.deleteProductOperationList(data)]).then(res => {
                            this.$message.success('删除成功!');
                            this.initEmps();
                        }).catch(() => {
                            this.$message.error('删除失败!');
                        })
                    })
                } else if (data.category === '制定标准') {
                    this.$confirm("此操作将永久删除【" + data.name + "】, 是否继续?").then(() => {
                        Promise.all([this.deleteStandardEmpMethod(data), this.deleteStandardOperationList(data)]).then(res => {
                            this.$message.success('删除成功!');
                            this.initEmps();
                        }).catch(() => {
                            this.$message.error('删除失败!');
                        })
                    })
                }
            },
        },

    }


</script>

<style scoped>

    .isMust {
        position: absolute;
        color: #F56C6C;
        top: 2px;
        left: -100px;
    }
</style>