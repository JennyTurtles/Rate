<template>
  <div class="transfer">
    <div class="transfer-panel-left">
      <p class="transfer-panel-header">
          <span></span>
          <el-checkbox @change="displayNoGroup" v-model="displayNoGroupCheck" style="float: left">仅未分组</el-checkbox>
        <span style="margin-right: 75px">{{titleTexts && titleTexts[0]}}</span>
        <span>{{leftSelection.length}}/{{leftTableData.length}}</span>
      </p>
      <div v-if="showQuery" >
        <el-form :inline="true" :model="leftQueryCondition" style="float: left" class="query-form">
          <slot name="leftCondition" v-bind:scope="leftQueryCondition"></slot>

          <el-form-item>
<!--            <el-button class="el-icon-search" type="primary" @click="onLeftQuerySubmit()">{{queryTexts[0]}}</el-button>-->
              <el-button class="el-icon-search" type="primary" @click="onLeftQuerySubmit" style="padding: 7px;margin-left: 5px"></el-button>
              <el-button class="el-icon-refresh" type="primary" @click="refresh" style="padding: 7px;margin-left: 5px"></el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
          ref="leftTable"
          size="small"
          @current-change="handleCurrentChangeColour"
          height="790px"
          :data="leftTableData"
          :row-key="tableRowKey"
          :row-class-name="handleRowStyle"
          @row-click="handleLeftRowClick"
          @selection-change="handleLeftSelectionChange"
          border
          >
<!--        <el-table-column-->
<!--            width="40px"-->
<!--            type="selection"-->
<!--            :selectable="handleSelectable"></el-table-column>-->
        <el-table-column
            sortable
            :sort-method="(a, b) => {
                if (col.label === '编号')
                      return Number(a.code)- Number(b.code)
                else if (col.label === '工号')
                    return Number(a.jobNumber)- Number(b.jobNumber)
                else{
                    // 按照拼音排序
                    return a[col.id].localeCompare(b[col.id], 'zh-Hans-CN', {sensitivity: 'accent'})
                }
            }"
            v-for="col in leftColumns"
            :prop="col.id"
            :key="col.id"
            :label="col.label"
            :width="col.width">
          <template slot-scope="scope">
            <slot v-bind:scope="{row: scope.row, col: col}">
              <span>{{scope.row[col.id]}}</span>
            </slot>
          </template>
        </el-table-column>
      </el-table>
<!--      <el-pagination-->
<!--          v-if="false"-->
<!--          @size-change="handleSizeChange"-->
<!--          @current-change="handleCurrentChange"-->
<!--          :current-page="pageIndex"-->
<!--          :page-sizes="[1000]"-->
<!--          :page-size="1000"-->
<!--          :pager-count="5"-->
<!--          :total="totalSize"-->
<!--          layout="total, sizes, prev, pager, next">-->
<!--      </el-pagination>-->
    </div>
    <div class="transfer-buttons">
      <el-button
          type="primary"
          :class="buttonClasses"
          :disabled="disabledLeftButton"
          @click.native="addToRight">
        <span v-if="buttonTexts[0] !== undefined" class="button-text"></span>
        <i class="el-icon-arrow-right"></i>
      </el-button>
      <el-button
          type="primary"
          :class="buttonClasses"
          :disabled="rightSelection.length === 0"
          @click.native="addToLeft">
        <i class="el-icon-arrow-left"></i>
        <span v-if="buttonTexts[1] !== undefined" class="button-text"></span>
      </el-button>
    </div>
    <div class="transfer-panel-right">
      <p class="transfer-panel-header">
          <span>{{rightSelection.length}}/{{rightTableData.length}}</span>
        <span>{{titleTexts && titleTexts[1]}}</span>
<!--          <el-button type="danger" style="position: absolute ;right: 70px;margin-top: 5px" @click="$parent.clearTransfer()">清空</el-button>-->
          <el-button type="success" style="position: absolute ;right: 5px;margin-top: 5px;padding: 7px" @click="$parent.import()">导入</el-button>
      </p>
      <el-table
          ref="rightTable"
          size="small"
          height="745px"
          :row-class-name="handleRowStyleForRight"
          :data="calcRightTableData"
          :row-key="tableRowKey"
          @row-click="handleRightRowClick"
          @selection-change="handleRightSelectionChange"
          border
          >
<!--        <el-table-column width="40px" type="selection"></el-table-column>-->
        <el-table-column
            v-for="col in rightColumns || leftColumns"
            :prop="col.id"
            :key="col.id"
            :label="col.label"
            :width="col.width">
          <template slot-scope="scope">
            <slot v-bind:scope="{row: scope.row, col: col}">
              <span>{{scope.row[col.id]}}</span>
            </slot>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
  import fa from "element-ui/src/locale/lang/fa";
  import da from "element-ui/src/locale/lang/da";

  export default {
    name: 'EltTransfer',
    props: {
      value: {
        type: Array,
        default() {
          return []
        }
      },
      // 显示条件查询
      showQuery: {
        type: Boolean,
        default: false
      },
      // 显示分页
      showPagination: {
        type: Boolean,
        default: false
      },
      // 左侧分页回调
      paginationCallBack: {
        type: Function,
        default: function () {
          return new Promise(((resolve, reject) => {
            try {
              resolve({total: 0, data: null})
            } catch {
              reject()
            }
          }))
        }
      },
      // 标题文本
      titleTexts: {
        type: Array,
        default() {
          return ['待选项', '已选项']
        }
      },
      // 按钮文本
      buttonTexts: {
        type: Array,
        default() {
          return []
        }
      },
      // 查询按钮文本
      queryTexts: {
        type: Array,
        default() {
          return ['查询','筛选']
        }
      },
      // 左侧参数
      leftColumns: {
        type: Array,
        default() {
          return []
        }
      },
      // 右侧参数
      rightColumns: {
        type: Array,
        default() {
          return undefined
        }
      },
      // 表格最小高度
      minHeight: {
        type: String,
        default: 'auto'
      },
      // 表格最大高度
      maxHeight: {
        type: String,
        default: 'auto'
      },
      // 表格行数据的Key
      tableRowKey: {
        type: Function,
        default(row) {
          return row && row && row.id
        }
      }
    },
    data() {
      return {
        // groupID:-1,
        // checkList : [],
        displayNoGroupCheck:false,
        leftTableData: [],
        rightTableData: this.value || [],
        pageIndex: 1,
        pageSize: 1000,
        totalSize: 0,
        leftSelection: [],
        rightSelection: [],
        leftQueryCondition: {},
        rightQueryCondition: {},
        rightConditionTemp: undefined
      }
    },
    created() {
      this.handlePaginationCallBack()
    },
    computed: {
      hasButtonTexts() {
        return this.buttonTexts.length === 2
      },
      buttonClasses() {
        return ['transfer-button', {'is-with-texts': this.hasButtonTexts}]
      },
      disabledLeftButton() {
        return !this.leftSelection.some(leftRow => !this.rightTableData.some(rightRow => this.checkObjectIsEqual(leftRow, rightRow)))
      },
      calcRightTableData() {
        if (this.showQuery && this.rightConditionTemp) {
          const conditionKeys = Object.keys(this.rightConditionTemp);
          return this.rightTableData.filter(data => {
            return conditionKeys.some(key => {
              const rowCellData = data[key];
              const condVal = this.rightConditionTemp[key].trim();
              if (rowCellData) {
                return String(rowCellData).indexOf(condVal) > -1
              }
              return true;
            })
          })
        }
        return this.rightTableData;
      }
    },
    methods: {
        handleLeftSelectionChange(selection) {
            this.leftSelection = selection
        },
        handleRightSelectionChange(selection) {
            this.rightSelection = selection
        },
        handleLeftRowClick(row) {
            if (!this.rightTableData.some(rightRow => this.checkObjectIsEqual(rightRow, row))) {
                this.$refs.leftTable.toggleRowSelection(row)
            }
        },
        handleRightRowClick(row) {
            this.$refs.rightTable.toggleRowSelection(row)
        },
        handleSizeChange(val) {
            this.pageSize = val
            this.handlePaginationCallBack()
        },
        handleCurrentChangeColour() {

        },
        handleCurrentChange(val) {
            this.pageIndex = val
            this.handlePaginationCallBack()
        },
        refresh() {
            this.leftQueryCondition = {}
            this.handlePaginationCallBack(true)
        },
        handlePaginationCallBack(flag) {
            if (this.showPagination && this.paginationCallBack) {
                const condition = {
                    pageIndex: this.pageIndex,
                    pageSize: this.pageSize,
                    ...this.leftQueryCondition
                }
                this.paginationCallBack.call(null, condition).then(result => { // 筛选
                    result.data = result.data.filter(data => {
                        const conditionKeys = Object.keys(this.leftQueryCondition);
                        return conditionKeys.every(key => {
                            const rowCellData = data[key.toString()];
                            const condVal = this.leftQueryCondition[key];
                            if (rowCellData) {
                                return String(rowCellData).indexOf(condVal) > -1
                            }
                            return true;
                        })
                    });
                    result.total = result.data.length;
                    if (result && Array.isArray(result.data)) {
                        this.leftTableData = result.data
                        this.totalSize = result.total
                    }
                    this.$nextTick(() => {
                            this.leftTableData.forEach(leftRow => {
                                const isHave = this.rightTableData.some(rightRow => this.checkObjectIsEqual(rightRow, leftRow))
                                this.$refs.leftTable.toggleRowSelection(leftRow, isHave)
                            })
                            if (this.displayNoGroupCheck){
                                this.leftTableData = this.leftTableData.filter(data => {
                                    return !this.rightTableData.some(rightRow => this.checkObjectIsEqual(rightRow, data))
                                })
                            }

                        }
                    )
                })
            }
        },
        handleRowStyle({row}) {
            if (this.rightTableData.some(rightRow => this.checkObjectIsEqual(rightRow, row))) {
                return 'gray-row'
            }

            // 如果row在this.leftSelection里就设置背景为蓝色
            if (this.leftSelection.some(leftRow => this.checkObjectIsEqual(leftRow, row)))
                return 'blue-row'
            // for (const item in this.checkList) {
            //   if (this.checkList[item].id == row.id && this.checkList[item].groupID != this.groupID){
            //     return 'warning-row'
            //   }
            // }
            return ''
        },
        handleRowStyleForRight({row}) {
            if (this.rightSelection.some(rightRow => this.checkObjectIsEqual(rightRow, row)))
                return 'blue-row'
            return ''
        },
        handleSelectable(row) {
            // for (const item in this.checkList) {
            //     if (this.checkList[item].id == row.id && this.checkList[item].groupID != this.groupID){
            //         return false
            //     }
            // }
            return !this.rightTableData.some(rightRow => this.checkObjectIsEqual(rightRow, row))
        },
        addToRight() {
            var addList = []
            for (const item of this.leftSelection) {
                const isHave = this.rightTableData.some(rightRow => this.checkObjectIsEqual(rightRow, item))
                if (!isHave) {
                    this.rightTableData.push(item)
                    addList.push(item)
                }
            }
            this.$emit('input', this.rightTableData)
            this.$parent.simpleAdd(addList)
        },
        addToLeft() {
            var delList = []
            this.rightSelection.forEach(item => {
                const index = this.rightTableData.findIndex(rightRow => this.checkObjectIsEqual(rightRow, item))
                if (index !== -1) {
                    this.rightTableData.splice(index, 1)
                    delList.push(item)
                    const leftRow = this.leftTableData.find(leftRow => this.checkObjectIsEqual(leftRow, item))
                    if (leftRow) {
                        this.$refs.leftTable.toggleRowSelection(leftRow, false)
                    }
                }
            })
            this.$parent.delete(delList)
            this.$emit('input', this.rightTableData)
        },
        onLeftQuerySubmit() {
            this.handlePaginationCallBack(true);
        },
        onRightQuerySubmit() {
            this.rightConditionTemp = JSON.parse(JSON.stringify(this.rightQueryCondition));
        },
        checkObjectIsEqual(rowObj1, rowObj2) {
            return this.tableRowKey(rowObj1) === this.tableRowKey(rowObj2)
        },
        clear() {
            this.rightTableData = [];
            this.$refs.leftTable.clearSelection();
            // for (const key in this.leftQueryCondition) {
            //   this.leftQueryCondition[key] = undefined;
            // }
            // for (const key in this.rightQueryCondition) {
            //   this.rightQueryCondition[key] = undefined;
            // }
            this.pageIndex = 1;
            this.handlePaginationCallBack();
        },
        displayNoGroup(flag) {
            if (flag) {
                // 仅显示不在右边表格中的行
                this.leftTableData = this.leftTableData.filter(data => {
                    return !this.rightTableData.some(rightRow => this.checkObjectIsEqual(rightRow, data))
                })
            }else {
                this.handlePaginationCallBack(true);
            }
        },
    }
  }
</script>

<style scoped>
  .transfer {
    font-size: 14px;
    display: flex;
    justify-content: center;
    align-items: center;

  }

  .el-icon-arrow-right, .el-icon-arrow-left {
    font-size: 40px;
    cursor: pointer;
  }

  .transfer-panel-left {
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    overflow: hidden;
    background: #FFF;
    display: inline-block;
    /*width: 50%;*/
    max-height: 100%;
    box-sizing: border-box;
    position: relative;
  }
  .transfer-panel-right {
      border: 1px solid #EBEEF5;
      border-radius: 4px;
      overflow: hidden;
      background: #FFF;
      display: inline-block;
      /*width: 40%;*/
      max-height: 100%;
      box-sizing: border-box;
      position: relative
  }
  .transfer-panel-header {
    height: 40px;
    line-height: 40px;
    background: #F5F7FA;
    margin: 0;
    padding-left: 15px;
    border-bottom: 1px solid #EBEEF5;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    color: #000;
  }

  .transfer-panel-header span:first-child {
    position: absolute;
    left: 15px;
  }
  .transfer-panel-header span:last-child {
      position: absolute;
      right: 15px;
  }
  .transfer-buttons {
    display: inline-block;
    vertical-align: middle;
    width: 50px;
  }

  .transfer-button {
    display: block;
    margin: 0 auto;
    padding: 10px;
    border-radius: 4px;
    color: #FFF;
    background-color: #409EFF;
    font-size: 0
  }


  .transfer-button .button-text {
    margin-left: 2px;
    margin-right: 5px;
  }

  .transfer-button.is-with-texts {
    border-radius: 4px
  }

  .transfer-button.is-disabled, .transfer-button.is-disabled:hover {
    border: 1px solid #DCDFE6;
    background-color: #F5F7FA;
    color: #C0C4CC
  }

  .transfer-button:first-child {
    margin-bottom: 10px
  }

  .transfer-button:nth-child(2) {
    margin: 0 auto;
  }

  .transfer-button i, .transfer-button span {
    font-size: 14px
  }

  .query-form {
    margin: 5px;
  }

</style>

<style>
  .query-form .el-form-item {
    margin-bottom: 0;
  }
  .el-table .warning-row {
      background: oldlace;
  }
  .el-table .blue-row {
      background: #e6f7ff;
  }
  .el-table .success-row {
      background: #f0f9eb;
  }

  .gray-row {
      color: #c6c5c5;
      cursor: not-allowed;
  }

</style>
