import React, { useEffect } from 'react';
import { Breadcrumb, Layout, Menu, theme } from 'antd';
import { Route, Router, Routes } from 'react-router';
import PermissionType from './pages/PermissionType/PermissionType';
import { Link, useNavigate } from 'react-router-dom';
import FreeUri from './pages/FreeUri/FreeUri'
const { Header, Content, Footer } = Layout;
// 菜单项
function getItem(label, key, icon, children, type) {
  return {
    key,
    icon,
    children,
    label,
    type,
  };
}

const items = [
  { key: '/permissiontype', label: <Link to='/permissiontype' style={{ fontSize: 15 }}>Permission Type</Link> },
  { key: '/free_uri', label: <Link to='/free_uri' style={{ fontSize: 15 }}>Free Uri</Link> }
]
const App = () => {
  const {
    token: { colorBgContainer, borderRadiusLG },
  } = theme.useToken();
  const navigate = useNavigate();

  // 在组件加载时自动导航到第一个路由
  useEffect(() => {
    navigate('/permissionType');
  }, []);

  return (
    <Layout>
      <Header
        style={{
          display: 'flex',
          alignItems: 'center',
        }}
      >
        <div className="demo-logo" />
        <Menu
          theme="dark"
          mode="horizontal"
          defaultSelectedKeys={['2']}
          items={items}
          style={{
            flex: 1,
            minWidth: 0,
          }}
        />
      </Header>
      <Content
        style={{
          padding: '0 48px',
        }}
      >
        <Breadcrumb
          style={{
            margin: '16px 0',
          }}
        >
        </Breadcrumb>
        <div
          style={{
            background: colorBgContainer,
            minHeight: 280,
            padding: 24,
            borderRadius: borderRadiusLG,
          }}
        >
          <Routes>
            <Route path='/permissionType' element={<PermissionType />}></Route >
            <Route path='/free_uri' element={<FreeUri />}></Route>
          </Routes>
        </div>
      </Content>
      <Footer
        style={{
          textAlign: 'center',
        }}
      >

      </Footer>
    </Layout>
  );
};
export default App;