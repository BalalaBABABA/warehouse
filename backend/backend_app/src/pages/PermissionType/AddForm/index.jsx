import { React, useEffect } from 'react'
import { Form, Select, Input, InputNumber } from 'antd'
import { useForm } from 'antd/es/form/Form'
import PropTypes from 'prop-types'
const Item = Form.Item

function AddForm(props) {

    const [form] = Form.useForm();
    const { columns, setForm } = props
    useEffect(() => {
        setForm(form)
    }, [props])

    return (
        <Form form={form} >
            {
                columns.map((c, index) => {

                    return (
                        <Item
                            rules={c.rules}
                            key={c.key}
                            label={c.title}
                            name={c.dataIndex}
                            labelCol={{ span: 5 }}>
                            <Input placeholder={`请输入${c.title}`} name={c.dataIndex}></Input>
                        </Item>
                    )
                })
            }
        </Form>
    )
}

// UpateForm.propTypes = {
//     category: PropTypes.any.isRequired,
//     setForm: PropTypes.func.isRequired
// }

export default AddForm