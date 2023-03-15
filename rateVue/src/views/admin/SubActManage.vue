<template>
  <div>
    <ActManage :mode="mode"></ActManage>
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
      mode:'adminSub',
    }
  },
  methods: {

  },
};
</script>


<style>

</style>