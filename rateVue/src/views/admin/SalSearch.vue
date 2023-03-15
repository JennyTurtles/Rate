<template>
  <div>
    <ActGroup :mode="mode"></ActGroup>
  </div>
</template>
<script>
import ActGroup from "@/components/ActManage.vue"
export default {
  name: "AdminActGroup",
  components:{
    'ActGroup':ActGroup,
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