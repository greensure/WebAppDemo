var vm = new Vue({
    el: "#app",
    data: {
        totalMoney: 0,
        productList: [],
        checkallFlag: false,
        delFlag: false,
        currProduct: ''//当前被选中的商品——删除用
    },
    //过滤器-money
    filters: {
        formatMoney : function(value) {
            // toFixed?
            return "￥ " + value.toFixed(2);
        }
    },
    // 实例创建完成,进行步骤
    mounted: function() {
        this.$nextTick(function() {
            // 保证this.$el已经插入文档
            this.cartView();
        })
    },
    methods: {
        cartView: function() {
            //this在在vue实例方法里，所有的this都指向vm实例，但是在某个函数内部他的作用域已经发生了变化，就不能直接使用this来点
            var _this = this;
            // this相当于在vm实例里面，resource插件基于vue，因此需要通过this来调用http方法，而非直接使用http
            // $http.get参数意义？
            // then ?
            this.$http.get('data/cartData.json', {'id': 123}).then(function(res) {
                // res : 最终拿到的结果
                // 把接口里面返回的list保存到data模型的变量里，从而用v-for来遍历列表
                _this.productList = res.data.result.list;
                // _this.productList = res.result.list;
            });
        },
        // 通过点击+-来改变数量
        // 参数意义？
        changeMoney: function(product, way){
            if(way > 0) {
                product.productQuantity++;
            }else {
                product.productQuantity--;
                if(product.productQuantity < 1){
                    product.productQuantity = 1;
                }
            }
            // 计算总金额
            this.calcTotalPrice();
        },
        calcTotalPrice: function() {
            // 总金额清零--》每次遍历之前需要把总金额清零，若不清零则每次操作所有的总金额都进行累加
            this.totalMoney = 0;
            this.productList.forEach((item, index)=>{
                // item.checked==true 说明商品选中了
                if(item.checked) {
                    // 计算总价
                    // 累加的过程
                    this.totalMoney += item.productQuantity * item.productPrice;
                }
            })
        },
        //勾选单个商品
        selectedProduct: function(item){
            var _this = this;
            // 如何判断一个对象里变量是否存在？
            //判断是否有checked数据项,如果没有则进行全局注册 
            if(typeof item.checked == 'undefined'){
                //全局注册
                Vue.set(item, "checked", true);
                //_this.$set(item, "checked", true);//局部注册
            } else {
                item.checked = !item.checked;
            }
              //计算总金额
            this.calcTotalPrice();
        },
        //勾选全部
        checkAll: function(flag) {
            this.checkallFlag = flag;
            this.productList.forEach((item, index) =>{
                // ES6的语法
                if(typeof item.checked == "undefined"){
                    this.$set(item, 'checked', this.checkallFlag);
                } else{
                    item.checked = this.checkallFlag;
                };
            })  
            // 单击 全选 之后要重新计算总金额
            // 计算总金额
            this.calcTotalPrice();              
        },
        // 删除框
        delConfirm: function(item){
            this.delFlag = true;
            // 把界面上选中的item赋值到currProduct？
            this.currProduct = item;
        },
        delProduct: function(item) {
            // 怎么通过Js去遍历到当前选中的对象呢？
            // 通过indexOf
            var index = this.productList.indexOf(this.currProduct);
            // 获取了索引之后怎么去删除商品呢？
            // splice：从当前索引开始删，删除1个元素
            this.productList.splice(index, 1);
            // 删除了之后，productList 的 v-for就会开始进行实时计算
            // 删除成功后要把delFlag重置为false
            this.delFlag = false;
        },

    },

});
//全局过滤器
Vue.filter('money', function(value, type) {
    return "¥ " + value.toFixed(2) + type;
});
