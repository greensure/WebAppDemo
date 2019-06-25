// 新建一个vue实例
new Vue({
    el: '.container', // 监控页面控制范围
    // 整个vue的模型，需要通过模型改变操作页面上的元素（dom）
    data: {
        limitNum: 3,
        addressList: [], //卡片地址
        currIndex: 0,
        shippingMethod: 1,//配送方式。默认值设为1
        delFlag: false,
        currAddress: '',
        addFlag: false,
        modifyFlag: false,
        modifyId: '',
        modifyName: '',
        modifyStreetAddress: '',
        modifyTel: ''
    },
    //mounted一般配合 nextTick 一起使用，因为mounted并不能代表vue实例已经插入dom节点里
    // nextTick实现数字化加载，完全保证dom已经插入进去，
    mounted: function(){
        this.$nextTick(function(){
            // 分割截取了3条数据
            this.getAddressList();
            // return this.addressList.slice(0, this.limitNum);
        });
    },
    // 实时计算:computed一般用于实时计算
    computed: {
        filterAddress: function() {
            // 分割截取3条数据
            // TODO 了解slice用法：对数组进行切割，slice返回全新数组
            return this.addressList.slice(0, this.limitNum);
        }
    },
    //methods定义所有的事件，需要调用的方法
    methods: {
        // 获取json数据列表
        getAddressList: function(){
            // 获取 address json数据
            // 
            this.$http.get('data/address.json').then(response=>{
                // response.data：拿到json数据
                var res = response.data;
                if(res.status == '0'){ //==0 说明请求成功
                    //将结果保存到this.addressList后，在address页面就可以进行遍历了
                    this.addressList = res.result;
                }
            });
        },
        // //加载更多(可用方法或者指令)
        loadMore: function() {
            if(this.limitNum == 3){
                // 加载更多
                this.limitNum = this.addressList.length;
            } else{
                // 隐藏
                this.limitNum = 3;
            }
        },
        // 设置为默认地址
        setDefault: function(addressId){
            // 遍历
            this.addressList.forEach(function(item, index){
                if(item.addressId == addressId){
                    //把当前选中的卡片改为true
                    item.isDefault = true;
                } else {
                    //把其他的卡片改为false
                    item.isDefault = false;
                }
            });
        },
        // 删除地址
        delConfirm: function(item){
            this.delFlag = true;
            this.currAddress = item;
        },
        //正式删除
        delAddress:function(){
            var index = this.addressList.indexOf(this.currAddress);
            this.addressList.splice(index, 1);
            this.delFlag = false;
        },
        // 修改窗口
        modifyMod: function(item){
            this.modifyFlag = true;
            this.modifyId = item.addressId;
            this.modifyName = item.userName;
            this.modifyStreetAddress = item.streetName;
            this.modifyTel = item.tel;

        },
        // 确认保存
        saveModify:function(addressId){
            this.addressList.forEach((item, index)=>{
                if(item.addressId == addressId){
                    item.userName = this.modifyName;
                    item.streetName = this.modifyStreetAddress;
                    item.tel = this.modifyTel;
                }
            });
        this.modifyFlag = false;
        }
    }

});