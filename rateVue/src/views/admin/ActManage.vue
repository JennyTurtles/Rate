<template>
  <div>
    <ActManage :mode="mode"></ActManage>
  </div>
</template>
<script>
import ActManage from "@/components/ActManage.vue"
export default {
  name: "AdminActGroup",
  components:{
    'ActManage':ActManage,
  },
  data() {
    return {
      mode:"admin"
    };
  },
  methods:{

  }
};
</script>