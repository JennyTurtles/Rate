菜单项数据加载成功之后，在前端有几个可以存放的地方：

1. sessionStorage
2. localStorage
**3. vuex**


table 导出 PDF
# 安装命令
1. npm install --save html2canvas
2. npm install jspdf --save
3. npm install jspdf-autotable  

# 修改autotable.js源代码
打开这个地址'node_modules\ jspdf-autotable\dist'，找到jspdf.plugin.autotable.js
ctrl+F 搜索fontStyle，将主题样式里面的所有head和foot的fontStyle改为normal
