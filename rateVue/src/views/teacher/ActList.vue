<template>

</template>

<script>
import {getRequest} from "@/utils/api";

export default {
  name: "actList",
  data() {
    return {
      timer:null,
      nowTime:new Date(),
      // tablelist:[],
      user:{},
      title: "",
      show: false,
      loading: false,
      activitiesList: [],
      tableHeader: [
        {
          prop: "activityLists[0].startDate",
          align: "center",
          label: "开始时间",
          width: "180px",
        },
        {
          prop: "activityLists[0].name",
          align: "center",
          label: "活动名称",
          width: "250px",
        },
        {
          prop: "groupName",
          align: "center",
          label: "组名",
          width: "180px",
        },
      ],
    };
  },
  computed: {

  },
  created() {

  },
  mounted(){

  },
  methods: {
    //表头样式
    rowClass() {
      return "background:#b3d8ff;color:black;font-size:10px;text-align:center";
    },
    showEnterView(data, index) {
      sessionStorage.removeItem('score')
      this.$router.push({
        path: "/Expert/peract/score",
        query: {
          keywords: index,
        },
      });
      // let routeUrl = this.$router.resolve({
      //   path: "/Expert/peract/score",
      //   // path:"/teacher/tperact/score",
      //   query: {
      //         keywords: index,
      //       },
      // })
      // window.open(routeUrl.href)
    },
  },
};
</script>
<style lang="css" scoped>
.acttitle {
  display: flex;
  justify-content: center;
  font-size: 20px;
  font-weight: bold;
  margin: 20px;

}
</style>