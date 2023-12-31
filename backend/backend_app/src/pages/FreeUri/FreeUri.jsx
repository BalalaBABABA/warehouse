import React, { useEffect, useState } from 'react';
import { Table, message, Button, Modal, Form, Input, Checkbox } from 'antd';
import { Header } from 'antd/es/layout/layout';
import { listAll, del, add, updateUri } from '../../api/FreeUriApi';
import Item from 'antd/es/list/Item';
import { UpdateUri } from '../../api/PermissionApi';



const App = () => {
    let [form] = Form.useForm()
    const [data, setData] = useState([])
    const [list, setList] = useState([])
    const [item, setItem] = useState('')
    // 0：不显示 1:显示添加 //2：显示修改
    const [status, setStatus] = useState(0)
    const [pagination, setPagination] = useState({
        current: 1, // 当前页码
        pageSize: 10, // 每页显示的数据量
    });
    useEffect(() => {
        getData()
    }, [])
    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            render: (text) => <div style={{ fontSize: 18 }}>{text}</div>,
        },
        {
            title: 'URI',
            dataIndex: 'uri',
            render: (text) => <div style={{ fontSize: 18 }}>{text}</div>,
        },
        {
            title: '操作',
            render: (text, record, index) => (
                <div style={{ fontSize: 18 }}>
                    <Button onClick={() => showUpdate(record)}>修改</Button>
                </div>
            ),
        }
    ];
    const rowSelection = {
        onChange: (selectedRowKeys, selectedRows) => {
            console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
            setList(selectedRows)
        },
        fixed: "right"
    };
    // 弹框
    const handelCancel = () => {
        // 隐藏框
        setStatus(0)
        form.resetFields(undefined)
    };

    const showAdd = () => {
        setStatus(1)
        form.resetFields(undefined)
    }
    const showUpdate = (item) => {
        setStatus(2)
        form.setFieldsValue({ uri: item.uri });
        setItem(item)
    }
    const getData = async () => {
        const { success, data, msg } = await listAll()
        if (success) {
            const newData = data.map((item) => ({ ...item, key: item.id }));
            setData(newData)
        } else {
            message.error(msg)
        }
    }

    const delList = async () => {

        const { success, msg } = await del(list)
        if (success) {
            setList([])
            getData()
        } else {
            message.error(msg)
        }

    }

    const addUri = async () => {
        form.validateFields().then(async (values) => {
            const { uri } = values
            // 隐藏框
            setStatus(0)
            // 发送添加请求
            const { success, msg } = await add(uri)
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
    const update = async () => {
        form.validateFields().then(async (values) => {
            const { uri } = values
            // 隐藏框
            setStatus(0)
            // 发送添加请求
            const { success, msg } = await updateUri({ id: item.id, uri })
            if (success) {
                getData();
                message.success('修改成功')
                // 清除输入数据
                form.resetFields(undefined)
            }
            else {
                message.error(msg)
            }

        }).catch(err => console.error("填写不符合要求"))
    }

    return (
        <div >
            <Header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', borderTopLeftRadius: '10px', borderTopRightRadius: '10px' }}>
                <div style={{ color: 'white', fontSize: 26 }}>
                    放行URI管理
                </div>
                <div style={{ display: 'flex' }}>
                    <Button style={{ fontSize: 20, lineHeight: '20px', marginRight: 10 }} danger type='primary' onClick={() => delList()}>
                        删除
                    </Button>
                    <Button style={{ fontSize: 20, lineHeight: '20px' }} type='primary' onClick={() => showAdd()}>
                        添加
                    </Button>
                </div>
            </Header>

            <Table
                rowSelection={{
                    type: 'checkbox',
                    ...rowSelection,
                    fixed: false
                }}

                selectedRows={list}
                columns={columns}
                dataSource={data}
                pagination={pagination}
            />

            <Modal
                title='添加放行URI'
                open={status === 1 ? true : false}
                onOk={addUri}
                onCancel={handelCancel}
            >
                <Form form={form}>
                    <Form.Item
                        name='uri'
                        label='URI'
                        labelCol={{ span: 5 }}
                        rules={[
                            {
                                required: true,
                                message: '必填'
                            }
                        ]}
                    >
                        <Input placeholder='请输入URI' />
                    </Form.Item>
                </Form>

            </Modal>


            <Modal
                title='修改放行URI'
                open={status === 2 ? true : false}
                onOk={update}
                onCancel={handelCancel}
            >
                <Form form={form}>
                    <Form.Item
                        name='uri'
                        label='URI'
                        labelCol={{ span: 5 }}
                        rules={[
                            {
                                required: true,
                                message: '必填'
                            }
                        ]}

                    >
                        <Input placeholder='请输入URI' />
                    </Form.Item>
                </Form>

            </Modal>
        </div>
    );
};
export default App;