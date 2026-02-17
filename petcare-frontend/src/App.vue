<template>
<!-- Login -->
<div v-if="!loggedIn" class="login-bg">
  <div class="login-box">
    <div class="login-logo"><h1>🐾 PetCare</h1><p>Management Platform</p></div>
    <div v-if="loginErr" class="login-err">{{ loginErr }}</div>
    <div class="login-field"><label>Username</label><input v-model="loginForm.username" placeholder="Enter username" @keyup.enter="doLogin"/></div>
    <div class="login-field"><label>Password</label><input v-model="loginForm.password" type="password" placeholder="Enter password" @keyup.enter="doLogin"/></div>
    <button class="login-btn" @click="doLogin">Login</button>
  </div>
</div>

<!-- Main -->
<div v-else class="layout">
  <div class="sidebar">
    <div class="sidebar-logo"><span>🐾</span> PetCare</div>
    <div v-for="g in menus" :key="g.key">
      <div class="mg-title" @click="g.open = !g.open">
        <span>{{ g.icon }} {{ g.label }}</span>
        <i :class="{ open: g.open }">▶</i>
      </div>
      <template v-if="g.open">
        <div v-for="c in g.children" :key="c.key" class="mi" :class="{ active: cur === c.key }" @click="go(c.key)">{{ c.label }}</div>
      </template>
    </div>
  </div>

  <div class="main">
    <div class="hd">
      <h3>{{ titles[cur] }}</h3>
      <div style="display:flex;align-items:center;gap:16px">
        <span style="font-size:13px;color:#666">👤 {{ loginUser }}</span>
        <a href="#" @click.prevent="doLogout" style="font-size:13px;color:#999;text-decoration:none">Logout</a>
      </div>
    </div>
    <div class="ct">

      <!-- DEPARTMENT -->
      <template v-if="cur === 'dept'">
        <div class="card" style="display:flex;justify-content:space-between;align-items:center">
          <b>Department List</b>
          <el-button type="primary" @click="deptDlg = true; deptEdit = null; deptFm.name = ''">+ Add Department</el-button>
        </div>
        <div class="card" style="padding:0">
          <table class="tbl">
            <thead><tr><th style="width:60px">No.</th><th>Department Name</th><th style="width:200px">Last Updated</th><th style="width:140px">Actions</th></tr></thead>
            <tbody>
              <tr v-for="(d, i) in depts" :key="d.id">
                <td>{{ i + 1 }}</td><td>{{ d.name }}</td><td>{{ d.updateTime }}</td>
                <td><a class="act" @click="deptEdit = d; deptFm.name = d.name; deptDlg = true">Edit</a><a class="del" @click="delDept(d.id)">Delete</a></td>
              </tr>
              <tr v-if="!depts.length"><td colspan="4" style="color:#999;padding:30px">No Data</td></tr>
            </tbody>
          </table>
        </div>
        <el-dialog v-model="deptDlg" :title="deptEdit ? 'Edit Department' : 'Add Department'" width="420px">
          <el-form label-width="100px"><el-form-item label="Dept Name"><el-input v-model="deptFm.name" placeholder="Enter department name"/></el-form-item></el-form>
          <template #footer><el-button @click="deptDlg = false">Cancel</el-button><el-button type="primary" @click="saveDept">Save</el-button></template>
        </el-dialog>
      </template>

      <!-- EMPLOYEE -->
      <template v-if="cur === 'emp'">
        <div class="card">
          <el-form :inline="true" style="display:flex;flex-wrap:wrap;gap:8px;align-items:center">
            <el-form-item label="Name" style="margin-bottom:0"><el-input v-model="eq.name" placeholder="Enter name" clearable style="width:150px"/></el-form-item>
            <el-form-item label="Gender" style="margin-bottom:0"><el-select v-model="eq.gender" placeholder="All" clearable style="width:100px"><el-option label="Male" :value="1"/><el-option label="Female" :value="2"/></el-select></el-form-item>
            <el-form-item label="Hire Date" style="margin-bottom:0"><el-date-picker v-model="eq.dates" type="daterange" range-separator="~" start-placeholder="Start" end-placeholder="End" value-format="YYYY-MM-DD" style="width:240px"/></el-form-item>
            <el-form-item style="margin-bottom:0">
              <el-button type="primary" @click="epg = 1; loadEmp()">Search</el-button>
              <el-button @click="eq.name = ''; eq.gender = null; eq.dates = null; epg = 1; loadEmp()">Reset</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="card" style="padding:0">
          <div style="padding:10px 16px"><el-button type="primary" size="small" @click="openEmpAdd">+ Add Employee</el-button></div>
          <div style="overflow-x:auto">
            <table class="tbl">
              <thead><tr><th style="width:50px">☑</th><th>Name</th><th style="width:70px">Gender</th><th style="width:60px">Avatar</th><th>Department</th><th>Job</th><th style="width:100px">Hire Date</th><th style="width:170px">Last Updated</th><th style="width:120px">Actions</th></tr></thead>
              <tbody>
                <tr v-for="e in emps" :key="e.id">
                  <td><input type="checkbox"/></td><td>{{ e.name }}</td><td>{{ e.gender === 1 ? 'Male' : 'Female' }}</td><td><span class="avatar">👤</span></td><td>{{ e.deptName || '-' }}</td><td>{{ jobMap[e.job] || '-' }}</td><td>{{ e.entryDate }}</td><td>{{ e.updateTime }}</td>
                  <td><a class="act" @click="openEmpEdit(e)">Edit</a><a class="del" @click="delEmp(e.id)">Delete</a></td>
                </tr>
                <tr v-if="!emps.length"><td colspan="9" style="color:#999;padding:30px">No Data</td></tr>
              </tbody>
            </table>
          </div>
          <div class="pager">
            <span>Total <b>{{ etot }}</b> records</span>
            <div><button :disabled="epg <= 1" @click="epg--; loadEmp()">‹ Prev</button><span class="cur-page">{{ epg }}</span><button :disabled="epg >= Math.ceil(etot / esz)" @click="epg++; loadEmp()">Next ›</button></div>
          </div>
        </div>
        <el-dialog v-model="empDlg" :title="empEdit ? 'Edit Employee' : 'Add Employee'" width="520px" @closed="resetEmpFm">
          <el-form :model="ef" label-width="100px">
            <el-form-item label="Username"><el-input v-model="ef.username"/></el-form-item>
            <el-form-item label="Name"><el-input v-model="ef.name"/></el-form-item>
            <el-form-item label="Gender"><el-radio-group v-model="ef.gender"><el-radio :label="1">Male</el-radio><el-radio :label="2">Female</el-radio></el-radio-group></el-form-item>
            <el-form-item label="Phone"><el-input v-model="ef.phone"/></el-form-item>
            <el-form-item label="Job"><el-select v-model="ef.job" style="width:100%"><el-option v-for="(v, k) in jobMap" :key="k" :label="v" :value="Number(k)"/></el-select></el-form-item>
            <el-form-item label="Salary"><el-input v-model.number="ef.salary" type="number"/></el-form-item>
            <el-form-item label="Hire Date"><el-date-picker v-model="ef.entryDate" type="date" value-format="YYYY-MM-DD" style="width:100%"/></el-form-item>
            <el-form-item label="Department"><el-select v-model="ef.deptId" style="width:100%"><el-option v-for="d in depts" :key="d.id" :label="d.name" :value="d.id"/></el-select></el-form-item>
          </el-form>
          <template #footer><el-button @click="empDlg = false">Cancel</el-button><el-button type="primary" @click="saveEmp">Save</el-button></template>
        </el-dialog>
      </template>

      <!-- CUSTOMER -->
      <template v-if="cur === 'ownerList'">
        <div class="card">
          <el-form :inline="true" style="display:flex;flex-wrap:wrap;gap:8px;align-items:center">
            <el-form-item label="Name" style="margin-bottom:0"><el-input v-model="oq.name" placeholder="Enter name" clearable style="width:150px"/></el-form-item>
            <el-form-item label="Gender" style="margin-bottom:0"><el-select v-model="oq.gender" placeholder="All" clearable style="width:100px"><el-option label="Male" :value="1"/><el-option label="Female" :value="2"/></el-select></el-form-item>
            <el-form-item label="Created" style="margin-bottom:0"><el-date-picker v-model="oq.dates" type="daterange" range-separator="~" start-placeholder="Start" end-placeholder="End" value-format="YYYY-MM-DD" style="width:240px"/></el-form-item>
            <el-form-item style="margin-bottom:0">
              <el-button type="primary" @click="opg = 1; loadOwn()">Search</el-button>
              <el-button @click="oq.name = ''; oq.gender = null; oq.dates = null; opg = 1; loadOwn()">Reset</el-button>
            </el-form-item>
          </el-form>
        </div>
        <div class="card" style="padding:0">
          <div style="padding:10px 16px"><el-button type="primary" size="small" @click="openOwnAdd">+ Add Customer</el-button></div>
          <div style="overflow-x:auto">
            <table class="tbl">
              <thead><tr><th style="width:50px">☑</th><th>Name</th><th style="width:70px">Gender</th><th style="width:130px">Phone</th><th>Email</th><th>Address</th><th style="width:170px">Created</th><th style="width:120px">Actions</th></tr></thead>
              <tbody>
                <tr v-for="o in owns" :key="o.id">
                  <td><input type="checkbox"/></td><td>{{ o.name }}</td><td><span class="tag" :class="o.gender === 1 ? 'tag-m' : 'tag-f'">{{ o.gender === 1 ? 'Male' : 'Female' }}</span></td><td>{{ o.phone }}</td><td>{{ o.email }}</td><td :title="o.address">{{ o.address }}</td><td>{{ o.createTime }}</td>
                  <td><a class="act" @click="openOwnEdit(o)">Edit</a><a class="del" @click="delOwn(o.id)">Delete</a></td>
                </tr>
                <tr v-if="!owns.length"><td colspan="8" style="color:#999;padding:30px">No Data</td></tr>
              </tbody>
            </table>
          </div>
          <div class="pager">
            <span>Total <b>{{ otot }}</b> records</span>
            <div><button :disabled="opg <= 1" @click="opg--; loadOwn()">‹ Prev</button><span class="cur-page">{{ opg }}</span><button :disabled="opg >= Math.ceil(otot / osz)" @click="opg++; loadOwn()">Next ›</button></div>
          </div>
        </div>
        <el-dialog v-model="ownDlg" :title="ownEdit ? 'Edit Customer' : 'Add Customer'" width="480px" @closed="resetOwnFm">
          <el-form :model="of" label-width="80px">
            <el-form-item label="Name"><el-input v-model="of.name"/></el-form-item>
            <el-form-item label="Phone"><el-input v-model="of.phone"/></el-form-item>
            <el-form-item label="Gender"><el-radio-group v-model="of.gender"><el-radio :label="1">Male</el-radio><el-radio :label="2">Female</el-radio></el-radio-group></el-form-item>
            <el-form-item label="Email"><el-input v-model="of.email"/></el-form-item>
            <el-form-item label="Address"><el-input v-model="of.address"/></el-form-item>
          </el-form>
          <template #footer><el-button @click="ownDlg = false">Cancel</el-button><el-button type="primary" @click="saveOwn">Save</el-button></template>
        </el-dialog>
      </template>

      <!-- EMP STATS -->
      <template v-if="cur === 'empStats'">
        <div class="charts-grid">
          <div class="card"><b style="display:block;margin-bottom:12px">Job Distribution</b><div id="ejc" class="chart-box"></div></div>
          <div class="card"><b style="display:block;margin-bottom:12px">Gender Distribution</b><div id="egc" class="chart-box"></div></div>
        </div>
      </template>

      <!-- OWNER STATS -->
      <template v-if="cur === 'ownerStats'">
        <div class="charts-grid">
          <div class="card"><b style="display:block;margin-bottom:12px">Gender Distribution</b><div id="ogc" class="chart-box"></div></div>
          <div class="card"><b style="display:block;margin-bottom:12px">Address Distribution</b><div id="oac" class="chart-box"></div></div>
        </div>
      </template>

    </div>
  </div>
</div>
</template>

<script setup>
import { ref, reactive, nextTick, onMounted, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'
import * as echarts from 'echarts'

// ==== LOGIN ====
const loggedIn = ref(false)
const loginForm = reactive({ username: '', password: '' })
const loginErr = ref('')
const loginUser = ref('Admin')

async function doLogin() {
  loginErr.value = ''
  if (!loginForm.username || !loginForm.password) { loginErr.value = 'Please enter username and password'; return }
  try {
    const r = await axios.post('/login', loginForm)
    if (r.data.code === 1 && r.data.data) {
      loggedIn.value = true
      loginUser.value = r.data.data.name || r.data.data.username
      if (r.data.data.token) localStorage.setItem('token', r.data.data.token)
      loadDept()
    } else { loginErr.value = 'Invalid username or password' }
  } catch (e) { loginErr.value = 'Login failed' }
}

function doLogout() {
  loggedIn.value = false
  loginForm.username = ''
  loginForm.password = ''
  loginErr.value = ''
  localStorage.removeItem('token')
}

// ==== NAV ====
const cur = ref('dept')
const menus = reactive([
  { key: 's', icon: '⚙️', label: 'System Management', open: true, children: [{ key: 'dept', label: 'Department' }, { key: 'emp', label: 'Employee' }] },
  { key: 'c', icon: '👥', label: 'Customer Management', open: true, children: [{ key: 'ownerList', label: 'Customer List' }] },
  { key: 't', icon: '📊', label: 'Statistics', open: true, children: [{ key: 'empStats', label: 'Employee Stats' }, { key: 'ownerStats', label: 'Customer Stats' }] }
])
const titles = { dept: 'Department Management', emp: 'Employee Management', ownerList: 'Customer List', empStats: 'Employee Statistics', ownerStats: 'Customer Statistics' }
const jobMap = { 1: 'Caretaker', 2: 'Veterinarian', 3: 'Insurance', 4: 'Support', 5: 'Admin' }

// ==== DEPT ====
const depts = ref([])
const deptDlg = ref(false)
const deptEdit = ref(null)
const deptFm = reactive({ name: '' })

async function loadDept() { try { const r = await axios.get('/depts'); if (r.data.code === 1) depts.value = r.data.data } catch (e) {} }
async function saveDept() { try { if (deptEdit.value) { await axios.put('/depts', { id: deptEdit.value.id, name: deptFm.name }) } else { await axios.post('/depts', { name: deptFm.name }) }; ElMessage.success('Saved'); deptDlg.value = false; loadDept() } catch (e) { ElMessage.error('Failed') } }
async function delDept(id) { try { await ElMessageBox.confirm('Delete?', 'Warning', { type: 'warning' }); await axios.delete(`/depts/${id}`); ElMessage.success('Deleted'); loadDept() } catch (e) {} }

// ==== EMP ====
const emps = ref([]); const etot = ref(0); const epg = ref(1); const esz = ref(10)
const eq = reactive({ name: '', gender: null, dates: null })
const empDlg = ref(false); const empEdit = ref(null)
const ef = reactive({ username: '', name: '', gender: 1, phone: '', job: 1, salary: null, entryDate: '', deptId: null })

async function loadEmp() { try { let u = `/emps?page=${epg.value}&pageSize=${esz.value}`; if (eq.name) u += `&name=${eq.name}`; if (eq.gender) u += `&gender=${eq.gender}`; if (eq.dates && eq.dates.length === 2) u += `&begin=${eq.dates[0]}&end=${eq.dates[1]}`; const r = await axios.get(u); if (r.data.code === 1) { emps.value = r.data.data.rows; etot.value = r.data.data.total } } catch (e) {} }
function openEmpAdd() { empEdit.value = null; resetEmpFm(); empDlg.value = true }
async function openEmpEdit(row) { try { const r = await axios.get(`/emps/${row.id}`); if (r.data.code === 1) { const d = r.data.data; empEdit.value = d; Object.assign(ef, { username: d.username || '', name: d.name, gender: d.gender, phone: d.phone, job: d.job, salary: d.salary, entryDate: d.entryDate, deptId: d.deptId }) } } catch (e) { empEdit.value = row; Object.assign(ef, row) }; empDlg.value = true }
function resetEmpFm() { Object.assign(ef, { username: '', name: '', gender: 1, phone: '', job: 1, salary: null, entryDate: '', deptId: null }) }
async function saveEmp() { try { if (empEdit.value) { await axios.put('/emps', { ...ef, id: empEdit.value.id }) } else { await axios.post('/emps', { ...ef }) }; ElMessage.success('Saved'); empDlg.value = false; loadEmp() } catch (e) { ElMessage.error('Failed') } }
async function delEmp(id) { try { await ElMessageBox.confirm('Delete?', 'Warning', { type: 'warning' }); await axios.delete(`/emps?ids=${id}`); ElMessage.success('Deleted'); loadEmp() } catch (e) {} }

// ==== OWNER ====
const owns = ref([]); const otot = ref(0); const opg = ref(1); const osz = ref(10)
const oq = reactive({ name: '', gender: null, dates: null })
const ownDlg = ref(false); const ownEdit = ref(null)
const of = reactive({ name: '', phone: '', gender: 1, email: '', address: '' })

async function loadOwn() { try { let u = `/owners?page=${opg.value}&pageSize=${osz.value}`; if (oq.name) u += `&name=${oq.name}`; if (oq.gender) u += `&gender=${oq.gender}`; if (oq.dates && oq.dates.length === 2) u += `&begin=${oq.dates[0]}&end=${oq.dates[1]}`; const r = await axios.get(u); if (r.data.code === 1) { owns.value = r.data.data.rows; otot.value = r.data.data.total } } catch (e) {} }
function openOwnAdd() { ownEdit.value = null; resetOwnFm(); ownDlg.value = true }
function openOwnEdit(row) { ownEdit.value = row; Object.assign(of, { name: row.name, phone: row.phone, gender: row.gender, email: row.email || '', address: row.address || '' }); ownDlg.value = true }
function resetOwnFm() { Object.assign(of, { name: '', phone: '', gender: 1, email: '', address: '' }) }
async function saveOwn() { try { if (ownEdit.value) { await axios.put('/owners', { ...of, id: ownEdit.value.id }) } else { await axios.post('/owners', { ...of }) }; ElMessage.success('Saved'); ownDlg.value = false; loadOwn() } catch (e) { ElMessage.error('Failed') } }
async function delOwn(id) { try { await ElMessageBox.confirm('Delete?', 'Warning', { type: 'warning' }); await axios.delete(`/owners?ids=${id}`); ElMessage.success('Deleted'); loadOwn() } catch (e) {} }

// ==== CHARTS ====
let ejc, egc, ogc, oac

function parseData(d) {
  if (Array.isArray(d)) return { names: d.map(i => i.name || i.pos || i.gender || ''), vals: d.map(i => i.num || i.value || 0) }
  if (d.nameList) return { names: d.nameList, vals: d.dataList }
  if (d.jobList) return { names: d.jobList, vals: d.dataList }
  return { names: [], vals: [] }
}

async function loadEmpCharts() {
  await nextTick()
  try {
    const [jR, gR] = await Promise.all([axios.get('/report/empJobData'), axios.get('/report/empGenderData')])
    const j = parseData(jR.data.data)
    if (ejc) { ejc.dispose(); ejc = null }
    ejc = echarts.init(document.getElementById('ejc'))
    ejc.setOption({ tooltip: { trigger: 'axis' }, grid: { left: 40, right: 20, bottom: 60, top: 20 }, xAxis: { type: 'category', data: j.names, axisLabel: { rotate: 20 } }, yAxis: { type: 'value', minInterval: 1 }, series: [{ type: 'bar', data: j.vals, barWidth: 40, itemStyle: { color: '#409EFF', borderRadius: [4, 4, 0, 0] } }] })
    const g = parseData(gR.data.data)
    if (egc) { egc.dispose(); egc = null }
    egc = echarts.init(document.getElementById('egc'))
    egc.setOption({ tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' }, legend: { bottom: 10 }, color: ['#409EFF', '#F56C6C', '#E6A23C'], series: [{ type: 'pie', radius: ['45%', '70%'], center: ['50%', '45%'], data: g.names.map((n, i) => ({ name: n, value: g.vals[i] })), label: { formatter: '{b}\n{d}%' }, itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 } }] })
  } catch (e) { console.error(e) }
}

async function loadOwnCharts() {
  await nextTick()
  try {
    const [gR, aR] = await Promise.all([axios.get('/report/ownerGenderData'), axios.get('/report/ownerAddressData')])
    const g = parseData(gR.data.data)
    if (ogc) { ogc.dispose(); ogc = null }
    ogc = echarts.init(document.getElementById('ogc'))
    ogc.setOption({ tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' }, legend: { bottom: 10 }, color: ['#409EFF', '#F56C6C', '#E6A23C'], series: [{ type: 'pie', radius: ['45%', '70%'], center: ['50%', '45%'], data: g.names.map((n, i) => ({ name: n, value: g.vals[i] })), label: { formatter: '{b}\n{d}%' }, itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 } }] })
    const a = parseData(aR.data.data)
    if (oac) { oac.dispose(); oac = null }
    oac = echarts.init(document.getElementById('oac'))
    oac.setOption({ tooltip: { trigger: 'axis' }, grid: { left: 40, right: 20, bottom: 80, top: 20 }, xAxis: { type: 'category', data: a.names, axisLabel: { rotate: 40, fontSize: 11 } }, yAxis: { type: 'value', minInterval: 1 }, series: [{ type: 'bar', data: a.vals, barWidth: 36, itemStyle: { color: '#67C23A', borderRadius: [4, 4, 0, 0] } }] })
  } catch (e) { console.error(e) }
}

// ==== NAV ====
function go(k) {
  cur.value = k
  if (k === 'dept') loadDept()
  if (k === 'emp') { loadDept(); loadEmp() }
  if (k === 'ownerList') loadOwn()
  if (k === 'empStats') loadEmpCharts()
  if (k === 'ownerStats') loadOwnCharts()
}

// ==== RESIZE ====
function onResize() { [ejc, egc, ogc, oac].forEach(c => c && c.resize()) }
onMounted(() => {
  window.addEventListener('resize', onResize)
  const savedToken = localStorage.getItem('token')
  if (savedToken) { loggedIn.value = true; loadDept() }
})
onUnmounted(() => { window.removeEventListener('resize', onResize) })
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box }
html, body, #app { height: 100%; width: 100% }
body { font-family: 'Microsoft YaHei', sans-serif; background: #f2f3f5 }

.layout { display: flex; height: 100vh }
.sidebar { width: 200px; background: #304156; color: #fff; flex-shrink: 0; overflow-y: auto }
.sidebar-logo { height: 60px; display: flex; align-items: center; justify-content: center; background: #263445; font-size: 18px; font-weight: 700 }
.sidebar-logo span { color: #409EFF; margin-right: 6px }
.mg-title { height: 48px; display: flex; align-items: center; padding: 0 20px; cursor: pointer; color: #bfcbd9; font-size: 14px; justify-content: space-between }
.mg-title:hover { background: #263445 }
.mg-title i { font-size: 12px; transition: transform .2s; font-style: normal }
.mg-title i.open { transform: rotate(90deg) }
.mi { height: 42px; display: flex; align-items: center; padding: 0 0 0 44px; cursor: pointer; font-size: 14px; color: #bfcbd9 }
.mi:hover { color: #fff; background: #263445 }
.mi.active { color: #fff; background: #409EFF }

.main { flex: 1; display: flex; flex-direction: column; overflow: hidden }
.hd { height: 50px; background: #fff; border-bottom: 1px solid #ddd; display: flex; align-items: center; justify-content: space-between; padding: 0 20px; flex-shrink: 0 }
.hd h3 { font-size: 16px; color: #333 }
.ct { flex: 1; padding: 16px; overflow: auto }
.card { background: #fff; border-radius: 4px; padding: 16px; margin-bottom: 12px; border: 1px solid #e8e8e8 }

table.tbl { width: 100%; border-collapse: collapse; table-layout: fixed }
table.tbl th, table.tbl td { border: 1px solid #e8e8e8; padding: 10px 8px; text-align: center; font-size: 13px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis }
table.tbl th { background: #f5f7fa; color: #333; font-weight: 600 }
table.tbl tr:nth-child(even) { background: #fafafa }
table.tbl tr:hover { background: #ecf5ff }
table.tbl .act { color: #409EFF; cursor: pointer; margin: 0 4px; text-decoration: none }
table.tbl .del { color: #F56C6C; cursor: pointer; margin: 0 4px; text-decoration: none }
.tag { display: inline-block; padding: 2px 8px; border-radius: 4px; font-size: 12px }
.tag-m { background: #ecf5ff; color: #409EFF }
.tag-f { background: #fde2e2; color: #F56C6C }
.avatar { width: 36px; height: 36px; border-radius: 50%; background: #eee; display: inline-block; text-align: center; line-height: 36px; font-size: 18px }

.pager { padding: 12px; display: flex; justify-content: center; align-items: center; gap: 20px; font-size: 13px; color: #666 }
.pager button { padding: 4px 12px; border: 1px solid #ddd; background: #fff; cursor: pointer; border-radius: 4px; font-size: 13px }
.pager button:hover { border-color: #409EFF; color: #409EFF }
.pager button:disabled { color: #ccc; cursor: default; border-color: #eee }
.pager .cur-page { padding: 4px 10px; background: #409EFF; color: #fff; border-radius: 4px }

.charts-grid { display: flex; gap: 16px }
.charts-grid .card { flex: 1 }
.chart-box { height: 340px; width: 100% }

.login-bg { height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%); position: relative; overflow: hidden }
.login-bg::before { content: ''; position: absolute; width: 400px; height: 400px; background: rgba(64,158,255,.08); border-radius: 50%; top: -100px; right: -100px }
.login-bg::after { content: ''; position: absolute; width: 300px; height: 300px; background: rgba(64,158,255,.06); border-radius: 50%; bottom: -80px; left: -80px }
.login-box { background: #fff; border-radius: 12px; padding: 40px; width: 400px; box-shadow: 0 20px 60px rgba(0,0,0,.3); position: relative; z-index: 1 }
.login-logo { text-align: center; margin-bottom: 30px }
.login-logo h1 { font-size: 28px; color: #1a1a2e; margin-bottom: 4px }
.login-logo p { font-size: 13px; color: #999 }
.login-field { margin-bottom: 18px }
.login-field label { display: block; font-size: 13px; color: #666; margin-bottom: 6px; font-weight: 500 }
.login-field input { width: 100%; padding: 10px 14px; border: 1px solid #dcdfe6; border-radius: 6px; font-size: 14px; outline: none; transition: border .2s }
.login-field input:focus { border-color: #409EFF; box-shadow: 0 0 0 2px rgba(64,158,255,.15) }
.login-btn { width: 100%; padding: 11px; background: #409EFF; color: #fff; border: none; border-radius: 6px; font-size: 15px; cursor: pointer; font-weight: 500; transition: background .2s; margin-top: 6px }
.login-btn:hover { background: #337ecc }
.login-err { color: #F56C6C; font-size: 13px; text-align: center; margin-bottom: 12px }
</style>