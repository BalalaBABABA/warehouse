import React, { Fragment, useState, useEffect } from 'react';
import { Collapse, theme, List, Button, message, Modal, Form } from 'antd';
import { CaretRightOutlined, MinusOutlined, RightOutlined, PlusOutlined, EditOutlined } from '@ant-design/icons';
import { AddPermissionType, DelPermissionType, listAll, AddResourceRequest, DelResourceRequest, DelUriRequest, AddUriRequest, UpdateResource, UpdateType, UpdateUri } from '../../api/PermissionApi';
import { FreeByPermissionTypeId, FreeByResourceId, FreeByUriId } from '../../api/FreeUriApi'
import AddForm from './AddForm'
import UpdateForm from './UpdateForm'
import { Header } from 'antd/es/layout/layout';
import { nanoid } from 'nanoid';
import { type } from '@testing-library/user-event/dist/type';
const { Panel } = Collapse;


const App = () => {
    let [form] = Form.useForm()
    // 表示添加/删除的框是否显示，0:都不显示，1:显示添加权限类型 2:显示删除权限类型 3:显示添加资源 4:显示删除资源 5:显示添加uri 6：显示删除uri
    const [status, setStatus] = useState(0)
    // 表示放行的框是否显示 0:不显示 1:显示放行资源 2:显示放行权限 3:显示放行uri
    const [freeStatus, setFreeStatus] = useState(0)
    // 表示修改的框是否显示 0:不显示 1:显示资源修改框 2:显示权限修改框 3:显示uri修改框
    const [mStatus, setMstatus] = useState(0)

    const [resourceId, setResourceId] = useState(0);
    const [typeId, setTypeId] = useState(0);
    const [uriId, setUriId] = useState(0)
    const [data, setData] = useState([]);
    const [activeKeys, setActiveKeys] = useState([]);
    const [item, setItem] = useState({})

    // 处理面板展开/折叠事件
    const handleCollapseChange = (keys) => {
        setActiveKeys(keys);
    };

    useEffect(() => {
        getData()
    }, [])


    // 添加权限类框的列
    const AddTypecolumns = [
        {
            title: "权限类型名称",
            dataIndex: "name",
            key: "name",
            rules: [
                {
                    required: true,
                    message: '必填'
                }
            ]
        }
    ]

    // 添加资源类框的列
    const AddResourcecolumns = [
        {
            title: "资源类型名称",
            dataIndex: "name",
            key: "name",
            rules: [
                {
                    required: true,
                    message: '必填'
                }
            ]
        },
        {
            title: "URI",
            dataIndex: "uri",
            key: "uri"
        },
        {
            title: "图标名称",
            dataIndex: "icon",
            key: "icon"
        },
        {
            title: "页面位置",
            dataIndex: "page",
            key: "page"
        }
    ]

    // 添加uri框的列
    const AddUricolumns = [
        {
            title: "URI",
            dataIndex: "name",
            key: "name",
            rules: [
                {
                    required: true,
                    message: '必填'
                }
            ]
        }
    ]

    // 弹框
    const handleCancel = () => {
        // 隐藏框
        setStatus(0)
        // 清楚输入数据
        form.resetFields(undefined)
    };
    // 弹框
    const handleFree = () => {
        // 隐藏框
        setFreeStatus(0)
        form.resetFields(undefined)
    };
    const handleModify = () => {
        setMstatus(0)
        form.resetFields(undefined)
    }

    // 显示添加权限框
    const showAddType = (resourceId) => {
        // 更新状态
        setStatus(1)
        // 更新资源id
        setResourceId(resourceId)
    }
    // 显示删除权限框
    const showDelType = (typeId) => {
        // 更新状态
        setStatus(2)
        // 更新类型id
        setTypeId(typeId)
    }
    // 显示添加资源框
    const showAddResource = () => {
        // 更新状态
        setStatus(3)
    }
    // 显示删除资源框
    const showDelResource = (resourceId) => {
        // 更新状态
        setStatus(4)
        // 更新类型id
        setResourceId(resourceId)
    }
    // 显示添加uri框
    const showAddUri = (typeId) => {
        // 更新状态
        setStatus(5)
        // 更新要添加的uri的typeId
        setTypeId(typeId)
    }
    // 显示删除uri框
    const showDelUri = (uriId) => {
        // 更新状态
        setStatus(6)
        // 更新删除的uri的Id
        setUriId(uriId)
    }

    //显示放行确认框
    const showFree = (type, id) => {
        switch (type) {
            case 'r':
                setFreeStatus(1)
                setResourceId(id)
                break
            case 't':
                setFreeStatus(2)
                setTypeId(id)
                break
            case 'u':
                setFreeStatus(3)
                setUriId(id)
                break
        }
    }

    //显示修改框
    const showUpdate = (type, id, item) => {

        switch (type) {
            case 'r':
                setMstatus(1)
                setResourceId(id)
                break
            case 't':
                setMstatus(2)
                setTypeId(id)
                break
            case 'u':
                setMstatus(3)
                setUriId(id)
                break
        }
        setItem(item)
    }


    //显示全部
    const getData = async () => {
        //发送获取请求
        const { success, data } = await listAll()
        if (success) {
            setData(data);
        }
        else {
            message.error('获取数据失败');
        }
    }
    //删除权限类型
    const DelPermission = async () => {
        const { success, msg } = await DelPermissionType(typeId);
        if (success) {
            getData();
            message.success('删除成功');
            setStatus(0)
        } else {
            message.error('删除失败');
        }
    }
    //添加权限类型
    const AddPermission = async () => {
        form.validateFields().then(async (values) => {
            const type = form.getFieldValue('name');

            // 隐藏框
            setStatus(0)
            // 发送添加请求
            const { success, msg } = await AddPermissionType(type, resourceId)
            if (success) {
                getData();
                message.success('添加成功')
                // 清除输入数据
                form.resetFields(undefined)
            }
            else {
                message.error(msg)
            }

        }).catch(err => console.error("填写不符合要求"))

    }
    //删除资源类型
    const DelResource = async () => {
        const { success, msg } = await DelResourceRequest(resourceId);
        if (success) {
            getData();
            message.success('删除成功');
            setStatus(0)
        } else {
            message.error('删除失败');
        }
    }
    //添加资源类型
    const AddResource = async () => {
        form.validateFields().then(async (values) => {
            // 发送添加请求
            const { success, msg } = await AddResourceRequest(values)
            if (success) {
                getData();
                // 隐藏框
                setStatus(0)
                message.success('添加成功')
                // 清除输入数据
                form.resetFields(undefined)
            }
            else {
                message.error(msg)
            }

        }).catch(err => console.error("填写不符合要求"))

    }
    //删除uri
    const DelUri = async () => {
        const { success, msg } = await DelUriRequest(uriId);
        if (success) {
            getData();
            message.success('删除成功');
            setStatus(0)
        } else {
            message.error('删除失败');
        }
    }
    //添加uri
    const AddUri = async () => {
        form.validateFields().then(async (values) => {
            const { name } = values
            // 发送添加请求
            const { success, msg } = await AddUriRequest(typeId, name)
            if (success) {
                getData();
                // 隐藏框
                setStatus(0)
                message.success('添加成功')
                // 清除输入数据
                form.resetFields(undefined)
            }
            else {
                message.error(msg)
            }

        }).catch(err => console.error("填写不符合要求"))

    }

    //发送放行请求
    const Free = async (type) => {
        let result = null;

        switch (type) {
            case 'r':
                result = await FreeByResourceId(resourceId);
                break;
            case 't':
                result = await FreeByPermissionTypeId(typeId);
                break
            case 'u':
                result = await FreeByUriId(uriId);
                break
        }
        const { success, msg, data } = result
        if (success) {
            message.info(data)
            setFreeStatus(0)
        } else {
            message.error(msg);
        }
    }

    //发送放行请求
    const Update = async (type) => {
        let result = null;
        form.validateFields().then(async (values) => {
            switch (type) {
                case 'r':
                    result = await UpdateResource({ id: resourceId, ...values });
                    break;
                case 't':
                    console.log(values)
                    result = await UpdateType({ id: typeId, ...values });
                    break
                case 'u':
                    result = await UpdateUri({ id: uriId, ...values });
                    break
            }
            const { success, msg, data } = result
            if (success) {
                message.info("修改成功")
                getData()
                form.resetFields(undefined)
                setMstatus(0)
            } else {
                message.error("修改失败");
            }
        }).catch(err => console.error("填写不符合要求"))
    }

    //渲染折叠面板
    const renderPanel = (data) => {
        const itemStyle = {
            display: "flex",
            alignItems: "center"
        };
        const spanStyle = {
            marginRight: 50,
            width: '200px'
        };
        const buttonStyle = {
            fontSize: 15
        };

        return data.map((item) => {
            if (item.permission) {
                return (
                    <Panel key={item.name} header={
                        <div style={itemStyle}>
                            <span style={{ flex: 1 }}>ID：{item.id}</span>
                            <span style={{ flex: 1 }}>资源名称：{item.name}</span>
                            <span style={{ flex: 1 }}>URI：{item.uri}</span>
                            <span style={{ flex: 2 }}>图标：{item.icon}</span>
                            <span style={{ flex: 2 }}>页面位置：{item.page}</span>
                            <Button type="primary" shape="circle" style={{ marginRight: 10 }} onClick={() => showDelResource(item.id)}>
                                <MinusOutlined />
                            </Button>
                            <Button type="primary" shape="circle" style={{ marginRight: 10 }} onClick={() => showAddType(item.id)}>
                                <PlusOutlined />
                            </Button>
                            <Button type="primary" shape="circle" style={{ marginRight: 10 }} onClick={() => showUpdate('r', item.id, item)}>
                                <EditOutlined />
                            </Button>
                            <Button type="primary" shape='circle' onClick={() => showFree('r', item.id)}>
                                <RightOutlined />
                            </Button>
                        </div>
                    } style={{ fontSize: 16 }}>
                        <Collapse>{renderPanel(item.permission)}</Collapse>
                    </Panel>
                );
            } else if (Array.isArray(item.uri)) {
                return (
                    <Panel header={
                        <div style={itemStyle}>
                            ID：<span style={spanStyle}>{item.id}</span>
                            权限类型：<span style={spanStyle}>{item.name}</span>
                            <Button type="primary" shape="circle" style={{ marginRight: 10 }} onClick={() => showDelType(item.id)}>
                                <MinusOutlined />
                            </Button>
                            <Button type="primary" shape="circle" style={{ marginRight: 10 }} onClick={() => showUpdate('t', item.id, item)}>
                                <EditOutlined />
                            </Button>
                            <Button type="primary" shape='circle' onClick={() => showFree('t', item.id)}>
                                <RightOutlined />
                            </Button>
                        </div>
                    } key={item.name} style={{ fontSize: 16 }}>
                        <List
                            size="small"
                            style={{ fontSize: 16 }}
                            dataSource={item.uri}
                            renderItem={(uriItem, index) =>
                                <React.Fragment key={nanoid()}>
                                    <List.Item style={{ fontSize: 16 }} key={nanoid()}>
                                        <span>{uriItem.id}</span>
                                        <span>{uriItem.name}</span>
                                        <span>
                                            <Button type="primary" shape="circle" danger style={buttonStyle} onClick={() => showDelUri(uriItem.id)}>
                                                <MinusOutlined />
                                            </Button>
                                            <Button type="primary" shape="circle" style={{ marginLeft: 10, marginRight: 10 }} onClick={() => showUpdate('u', uriItem.id, uriItem)}>
                                                <EditOutlined />
                                            </Button>
                                            <Button type="primary" shape='circle' onClick={() => showFree('u', uriItem.id)}>
                                                <RightOutlined />
                                            </Button>
                                        </span>
                                    </List.Item>
                                    {
                                        index === item.uri.length - 1 ?
                                            <List.Item style={{ fontSize: 16 }} key={nanoid()}>
                                                <Button type="primary" shape="circle" style={{ ...buttonStyle, marginLeft: 'auto' }} onClick={() => showAddUri(item.id)}>
                                                    <PlusOutlined />
                                                </Button>
                                            </List.Item> : ""
                                    }
                                </React.Fragment>
                            }>
                            {
                                item.uri.length === 0 ?
                                    <List.Item style={{ fontSize: 16, display: 'flex' }} key={nanoid()}>
                                        <Button type="primary" shape="circle" style={{ ...buttonStyle, marginLeft: 'auto' }} onClick={() => showAddUri(item.id)}>
                                            <PlusOutlined />
                                        </Button>
                                    </List.Item> : ""
                            }
                        </List >

                    </Panel >
                );
            } else {
                return (
                    <Panel key={item.name} header={
                        <div style={itemStyle}>
                            <span style={{ flex: 1 }}>ID：{item.id}</span>
                            <span style={{ flex: 1 }}>资源名称：{item.name}</span>
                            <span style={{ flex: 1 }}>URI：{item.uri}</span>
                            <span style={{ flex: 2 }}>图标：{item.icon}</span>
                            <span style={{ flex: 2 }}>页面位置：{item.page}</span>
                            <Button type="primary" shape="circle" style={{ marginRight: 10 }} resourceId={item.id} >
                                <MinusOutlined />
                            </Button>
                            <Button type="primary" shape="circle" resourceId={item.id} onClick={() => showAddType(item.id)}>
                                <PlusOutlined />
                            </Button>
                        </div>
                    } style={{ fontSize: 16 }}></Panel>
                );
            }
        });
    };

    return (
        <Fragment>
            <Header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderTopLeftRadius: '10px', borderTopRightRadius: '10px' }}>
                <div style={{ color: 'white', fontSize: 26 }}>
                    权限管理
                </div>
                <Button style={{ fontSize: 20, display: 'flex', alignItems: 'center' }} onClick={() => showAddResource()}>
                    添加资源
                </Button>
            </Header>

            <Collapse
                activeKey={activeKeys}
                onChange={handleCollapseChange}
                bordered={false}
                expandIcon={({ isActive }) => <CaretRightOutlined rotate={isActive ? 90 : 0} />}
                style={{ fontSize: 16 }}
            >
                {renderPanel(data)}

            </Collapse>
            <Modal
                title={status === 1 ? "添加权限类型" : (status === 3 ? "添加资源类型" : "添加URI")}
                open={status === 1 || status === 3 || status === 5 ? true : false}
                onOk={status === 1 ? AddPermission : (status === 3 ? AddResource : AddUri)}
                onCancel={handleCancel}
            >
                <AddForm columns={status === 1 ? AddTypecolumns : (status === 3 ? AddResourcecolumns : AddUricolumns)} setForm={(newform) => { form = newform }} />
            </Modal>


            <Modal
                title={mStatus === 1 ? "修改资源类型" : (mStatus === 2 ? "修改权限类型" : "修改URI")}
                open={mStatus === 1 || mStatus === 2 || mStatus === 3 ? true : false}
                onOk={mStatus === 1 ? () => Update('r') : (mStatus === 2 ? () => Update('t') : () => Update('u'))}
                onCancel={handleModify}
            >
                <UpdateForm item={item} columns={mStatus === 1 ? AddResourcecolumns : (mStatus === 2 ? AddTypecolumns : AddUricolumns)} setForm={(newform) => { form = newform }} />
            </Modal>

            <Modal
                title={status === 2 ? "删除权限类型" : (status === 4 ? "删除资源类型" : "删除URI")}
                open={status === 2 || status === 4 || status === 6 ? true : false}
                okText='确定'
                cancelText='取消'
                onOk={status === 2 ? DelPermission : (status === 4 ? DelResource : DelUri)}
                onCancel={handleCancel}
            >
                确认要删除吗？
            </Modal>

            <Modal
                title="放行"
                open={freeStatus === 1 || freeStatus === 2 || freeStatus === 3 ? true : false}
                okText='确定'
                cancelText='取消'
                onOk={freeStatus === 1 ? () => Free('r') : (freeStatus === 2 ? () => Free('t') : () => Free('u'))}
                onCancel={handleFree}
            >
                确认要放行吗？
            </Modal>
        </Fragment>
    );
};

export default App;
