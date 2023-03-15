<template>
  <div>
    <ActManage :mode="mode" :activityID="activityID" :actName="actName" :group-name="groupName" :groupID="groupID"></ActManage>
  </div>
</template>
<script>
import ActManage from "@/components/ActManage.vue"
export default {
  name: "SubActManage",
  components:{
    'ActManage':ActManage,
  },
  data()  {
    return {
      mode:'secretarySub',
      activityID:-1,
      actName:"",
      groupName:"",
      groupID:-1,
    }
  },
  created() {
    this.activityID = this.$route.query.id
    this.actName = this.$route.query.actName;
    this.groupName = this.$route.query.groupName;
    this.groupID = this.$route.query.groupID;
  },
  methods: {

  },
};
</script>


<style>

</style>