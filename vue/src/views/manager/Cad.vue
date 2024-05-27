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
                <div class="three-canvas" ref="threeTarget"></div>
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

import { ThreeEngine } from '@/utils/TEngine'
import { allBaseObject } from '@/utils/TBaseObject'
import { allLights } from '@/utils/TLights'
import { allHelper } from '@/utils/THelper'
import { STLLoader } from 'three/examples/jsm/loaders/STLLoader.js';
import * as THREE from "three";

export default {
    name: "Cad",
    methods:{
        logout() {
            localStorage.removeItem('honey-user')  // 清除当前的token和用户数据
            this.$router.push('/login')
        },
        handleCollapse() {
            this.isCollapse = !this.isCollapse
            this.asideWidth = this.isCollapse ? '64px' : '200px'
            this.collapseIcon = this.isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'
        },
    },
    data(){
      return{
          ThreeEngine: null,
          user: JSON.parse(localStorage.getItem('honey-user') || '{}'),
          historyData:[],  // 所有的数据
          text:'',
          username: '',
          title: '',
          form: {},
      }
    },
    mounted() {
        this.historyData.push({text:'create a cube'});
        this.ThreeEngine = new ThreeEngine(this.$refs.threeTarget)
        this.ThreeEngine.addObject(...allBaseObject)  // 添加基础模型
        this.ThreeEngine.addObject(...allLights)  // 添加光线
       // this.ThreeEngine.addObject(...allHelper)   // 添加辅助



    }
}
</script>

<style scoped>
.three-canvas {
    width: 100%;
    height: 100%;
    overflow: hidden;
    background-color: #d6eaff;
}
html, body {
    width: 100%;
    height: 100%;
    margin: 0;
    padding: 0;
    overflow: hidden; /* 防止出现滚动条 */
}
.header-ziti{

    background-color: #ffffff;
    justify-content: space-between;
    color: #000000;
    height: 140px;
}
</style>