<template>
  <div>
    <ActManage :mode="mode" :activityID="activityID"></ActManage>
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
    }
  },
  created() {
    this.activityID = this.$route.query.id
  },
  methods: {

  },
};
</script>


<style>

</style>