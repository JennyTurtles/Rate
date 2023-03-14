<template>
  <div>
    <ActList :mode="mode"></ActList>
  </div>
</template>
<script>
import ActList from "@/components/ActGroup.vue"
export default {
  name: "AdminActList",
  components:{
    'ActList':ActList,
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