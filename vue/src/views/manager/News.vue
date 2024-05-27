<template>
    <div>
        <el-container style="height: 700px; border: 1px solid #eee">
            <el-aside width="200px" style="background-color: #ffffff; border: 1px solid #eee">
                <img src="@/assets/logo1.png" style="width:200px; height: 60px ">
                <el-table
                    :data="historyData" style="width: 100%">
                    <el-table-column
                        prop="text" label="今日" width="180">
                    </el-table-column>
                </el-table>
            </el-aside>
            <el-main>
                <el-container>
                    <el-header class="header-ziti">
                        <div style="border: #666666">
                            <img src="@/assets/logo1.png" style="width:400px; height: 100px ">
<!--                            <span>%     多智鲁班</span>-->
                        </div>
                    </el-header>
                    <el-main>
                                <el-input
                                    v-model.lazy="text"
                                    style="width: 70%;"
                                    size="medium"
                                    placeholder="create a cube"
                                >
                                    <template #suffix>
                                        <el-button @click="setHistory" type="primary" style="height: 25px;">SUBMIT→</el-button>
                                    </template>
                                </el-input>

                        <el-card style="width: 1100px" shadow="always">
                            <div slot="header">
                                <img src="@/assets/logo3.png" style="width:120px; height: 30px ">
<!--                                <span class="ziti">Example：</span>-->
                            </div>
                            <ul>
                                <el-row :gutter="20">
                                    <el-col :span="12">
                                        <el-card style="width: 480px" shadow="always">a plate with 4 holes near each corner and rounded corners</el-card>
                                    </el-col>
                                    <el-col :span="12">
                                        <el-card style="width: 480px" shadow="always">a 3x6 lego</el-card>
                                    </el-col>
                                </el-row>
                                <el-row :gutter="20">
                                    <el-col :span="12">
                                        <el-card style="width: 480px" shadow="always">a 1/2 inch gear with 21 teeth</el-card>
                                    </el-col>
                                    <el-col :span="12">
                                        <el-card style="width: 480px" shadow="always">5-sided star</el-card>
                                    </el-col>
                                </el-row>
                            </ul>
                        </el-card>


                        <el-card class="box-card">
                            <div slot="header" class="clearfix">
                                <img src="@/assets/logo2.png" style="width:80px; height: 50px ">
                                <img src="@/assets/logo4.png" style="width:200px; height: 30px ">
<!--                                <span class="ziti">Our Suggestions：</span>-->
                            </div>
                            <ul>
                                <li>用英文描述具有良好几何形状的物体，而不是像“美丽“或”宇宙“这样对于我们模型来说抽象的概念，除非你只是想看 看它会做出什么奇形怪状的东西</li>
                                <li>总可能明确地描述，比如说你想要一个带有4个孔的大盘子，然后说明你希望孔位于何处以及每个孔的直径有多大</li>
                                <li>目前我们训练的深度学习模型更善于一次性表示单个物体而不是组合物</li>
                            </ul>
                        </el-card>

                    </el-main>

                </el-container>
            </el-main>

        </el-container>
        <el-container>
            <el-header style="border: 1px solid #eee">
                <i :class="collapseIcon" style="font-size: 26px" @click="handleCollapse"></i>
                <div style="flex: 1; width: 0; display: flex; align-items: center">
                    <el-dropdown placement="bottom">
                        <div style="display: flex; align-items: center; cursor: default">
                            <img :src="user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="" style="width: 40px; height: 40px; border-radius: 50%; margin: 0 5px">
                            <span>{{ user.name }}</span>
                        </div>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item @click.native="$router.push('/person')">个人信息</el-dropdown-item>
                            <el-dropdown-item @click.native="$router.push('/password')">修改密码</el-dropdown-item>
                            <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
            </el-header>
        </el-container>


    </div>
</template>

<script>
import E from "wangeditor"
import hljs from 'highlight.js'
import storage from "good-storage";
export default {
    name: "News",
    data() {
        return {
            text:'',
            historyData:[],  // 所有的数据
            username: '',
            title: '',
            form: {},
            user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
            count: "",

        }
    },
    mounted() {
        this.load()
    },
    methods: {
        setHistory(){
          this.historyData.push({text:this.text});
          this.text ='';
          // this.$router.push('/cad');
            const timejump = 4;
            if (!this.timer) {
                this.count = timejump;
                this.show = false;
                this.timer = setInterval(() => {
                    if (this.count > 0 && this.count <= timejump) {
                        this.count--;
                    } else {
                        this.show = true;
                        clearInterval(this.timer);
                        this.timer = null;
                        //跳转的页面写在此处
                        this.$router.push({
                            path: '/cad'
                        });
                    }
                }, 1000)
            }
        },
        getHistory(){
          this.historyData = storage.get('historyData',[]);
        },
        showContent(content) {
            this.content = content
            this.fromVisible1 = true
        },
        logout() {
            localStorage.removeItem('honey-user')  // 清除当前的token和用户数据
            this.$router.push('/login')
        },
        handleCollapse() {
            this.isCollapse = !this.isCollapse
            this.asideWidth = this.isCollapse ? '64px' : '200px'
            this.collapseIcon = this.isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'
        },

    }
}
</script>

<style scoped>
.ziti{
    font-weight: bold;
}
.header-ziti{

    background-color: #ffffff;
    justify-content: space-between;
    color: #000000;
    height: 140px;
}

.footer-user {
    border: 0px;
}

.button{
    display: inline-block;

}
</style>