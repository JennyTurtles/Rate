<template>
  <div>
    <ActManage :mode="mode"></ActManage>
  </div>
</template>
<script>
import ActManage from "@/components/ActManage.vue"
export default {
  name: "SecActManage",
  components:{
    'ActManage':ActManage,
  },
  data() {
    return {
      mode:"secretary"
    };
  },
  methods:{

  }
};
</script>