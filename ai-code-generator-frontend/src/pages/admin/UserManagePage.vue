<template>
  <div id="userManagePage">
    <!-- 搜索表单 -->
    <a-form layout="inline" :model="searchParams" @finish="doSearch">
      <a-form-item label="账号">
        <a-input v-model:value="searchParams.userAccount" placeholder="输入账号" />
      </a-form-item>
      <a-form-item label="用户名">
        <a-input v-model:value="searchParams.userName" placeholder="输入用户名" />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
    </a-form>
    <a-divider />
    <!-- 表格 -->
    <a-table
      :columns="columns"
      :data-source="data"
      :pagination="pagination"
      @change="doTableChange"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'userAvatar'">
          <a-image :src="record.userAvatar" :width="120" />
        </template>
        <template v-else-if="column.dataIndex === 'userRole'">
          <div v-if="editableData[record.id]">
            <a-select v-model:value="editableData[record.id].userRole" style="width: 120px">
              <a-select-option value="admin">管理员</a-select-option>
              <a-select-option value="user">普通用户</a-select-option>
            </a-select>
          </div>
          <div v-else>
            <div v-if="record.userRole === 'admin'">
              <a-tag color="green">管理员</a-tag>
            </div>
            <div v-else>
              <a-tag color="blue">普通用户</a-tag>
            </div>
          </div>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-button type="link" @click="showUserDetail(record.id)">详情</a-button>
          <a-button type="link" @click="showEditUser(record.id)">编辑</a-button>
          <a-button danger @click="showDeleteConfirm(record.id)">删除</a-button>
        </template>
      </template>
    </a-table>
    <!--删除弹窗-->
    <a-modal
      title="确认删除"
      :open="isModelVisible"
      @ok="doDelete"
      @cancel="isModelVisible = false"
      ok-text="确认"
      cancel-text="取消"
    >
      <p>确认删除该用户吗？</p>
    </a-modal>
    <!-- 详情弹窗 -->
    <a-modal
      title="用户详情"
      :open="isDetailVisible"
      @ok="isDetailVisible = false"
      @cancel="isDetailVisible = false"
      :width="800"
      :body-style="{ padding: '32px' }"
      :cancel-button-props="{ style: { display: 'none' } }"
    >
      <a-descriptions :column="detailColumns">
        <a-descriptions-item label="id">{{ detailData.id }}</a-descriptions-item>
        <a-descriptions-item label="账号">{{ detailData.userAccount }}</a-descriptions-item>
        <a-descriptions-item label="用户名">{{ detailData.userName }}</a-descriptions-item>
        <a-descriptions-item label="头像">
          <a-image :src="detailData.userAvatar" :width="120" />
        </a-descriptions-item>
        <a-descriptions-item label="简介">{{ detailData.userProfile }}</a-descriptions-item>
        <a-descriptions-item label="用户角色">
          <div v-if="detailData.userRole === 'admin'">
            <a-tag color="green">管理员</a-tag>
          </div>
          <div v-else>
            <a-tag color="blue">普通用户</a-tag>
          </div>
        </a-descriptions-item>
        <a-descriptions-item label="创建时间">
          {{ dayjs(detailData.createTime).format('YYYY-MM-DD HH:mm:ss') }}
        </a-descriptions-item>
      </a-descriptions>
    </a-modal>
    <!-- 编辑弹窗 -->
    <a-modal
      title="编辑用户"
      :open="isEditVisible"
      @ok="handleEditSubmit"
      @cancel="isEditVisible = false"
      :width="800"
      :body-style="{ padding: '32px' }"
    >
      <a-form
        :model="editForm"
        :rules="editRules"
        ref="editFormRef"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 18 }"
      >
        <a-form-item label="用户名" name="userName">
          <a-input v-model:value="editForm.userName" />
        </a-form-item>
        <a-form-item label="头像" name="userAvatar">
          <a-input v-model:value="editForm.userAvatar" />
        </a-form-item>
        <a-form-item label="简介" name="userProfile">
          <a-input v-model:value="editForm.userProfile" />
        </a-form-item>
        <a-form-item label="用户角色" name="userRole">
          <a-select v-model:value="editForm.userRole">
            <a-select-option value="admin">管理员</a-select-option>
            <a-select-option value="user">普通用户</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { message } from 'ant-design-vue'
import { ref, reactive, computed } from 'vue'
import { deleteUser, listUserVoByPage, updateUser } from '@/api/userController.ts'
import dayjs from 'dayjs'
import type { UnwrapRef } from 'vue'

// 控制弹窗
const isDetailVisible = ref(false)
const isEditVisible = ref(false)
const detailData = ref<API.UserVO | null>(null)
const editForm = reactive<API.UserUpdateRequest>({
  id: 0,
  userName: '',
  userAvatar: '',
  userProfile: '',
  userRole: '',
})

// 控制删除弹窗
const isModelVisible = ref(false)
const userIdToDelete = ref('')

// 表格列
const columns = [
  {
    title: 'id',
    dataIndex: 'id',
    align: 'center',
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
    align: 'center',
  },
  {
    title: '用户名',
    dataIndex: 'userName',
    align: 'center',
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
    align: 'center',
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
    align: 'center',
    width: 200,
    ellipsis: true,
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
    align: 'center',
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    align: 'center',
  },
  {
    title: '操作',
    key: 'action',
    align: 'center',
  },
]

// 数据
const data = ref<API.UserVO[]>([])
const total = ref(0)
const editableData: UnwrapRef<Record<string, API.UserVO>> = reactive({})

// 搜索条件
const searchParams = reactive<API.UserQueryRequest>({
  pageNum: 1,
  pageSize: 10,
})

// 获取数据
const fetchData = async () => {
  const res = await listUserVoByPage({
    ...searchParams,
  })
  if (res.data.data) {
    data.value = res.data.data.records ?? []
    total.value = res.data.data.totalRow ?? 0
  } else {
    message.error('获取数据失败，' + res.data.message)
  }
}

// 分页参数
const pagination = computed(() => {
  return {
    current: searchParams.pageNum ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value, // 确保total是数值类型
    showSizeChanger: true,
    showTotal: (total: number) => `共 ${total} 条`, // 确保total是数值类型
  }
})

const doTableChange = (page: any) => {
  searchParams.pageNum = page.current
  searchParams.pageSize = page.pageSize
  fetchData()
}

// 获取数据
const doSearch = () => {
  // 重置页码
  searchParams.pageNum = 1
  fetchData()
}

// 显示删除弹窗
const showDeleteConfirm = (id: string) => {
  userIdToDelete.value = id
  isModelVisible.value = true
}

// 确认删除
const doDelete = async () => {
  await doDeleteUser(userIdToDelete.value)
  isModelVisible.value = false
}

// 删除用户
const doDeleteUser = async (id: string) => {
  if (!id) {
    return
  }
  // 调用接口
  const res = await deleteUser({ id })
  if (res.data.code === 0) {
    message.success('删除成功')
    // 刷新页面
    fetchData()
  } else {
    message.error('删除失败，' + res.data.message)
  }
}

// 显示详情弹窗
const showUserDetail = (id: number) => {
  isDetailVisible.value = true
  // 调用接口
  const res = data.value.find((item) => item.id === id)
  if (res) {
    detailData.value = res
  }
}

const showEditUser = (id: number) => {
  const user = data.value.find((item) => item.id === id)
  if (user) {
    editForm.id = user.id
    editForm.userName = user.userName || ''
    editForm.userAvatar = user.userAvatar || ''
    editForm.userProfile = user.userProfile || ''
    editForm.userRole = user.userRole || 'user'
    isEditVisible.value = true
  }
}

const handleEditSubmit = async () => {
  try {
    const res = await updateUser(editForm)
    if (res.data.code === 0) {
      message.success('更新成功')
      isEditVisible.value = false
      fetchData()
    } else {
      message.error('更新失败: ' + res.data.message)
    }
  } catch (error) {
    message.error('更新失败')
    console.error('更新用户错误', error)
  }
}
// 定义detailColumns和editRules
const detailColumns = [
  { span: 3 },
  { span: 3 },
]

const editRules = {
  userName: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度2-20个字符', trigger: 'blur' }
  ],
  userAvatar: [
    { pattern: /^(https?:\/\/).*/, message: '请输入有效的URL地址', trigger: 'blur' }
  ],
  userProfile: [
    { max: 200, message: '简介最多200个字符', trigger: 'blur' }
  ],
  userRole: [
    { required: true, message: '请选择用户角色', trigger: 'change' },
  ],
}

// 页面加载时请求一次
fetchData()


</script>

<style>
#userManagePage {
  width: 1200px;
}

.ant-table-cell {
  white-space: normal;
  word-break: break-word;
}
</style>
