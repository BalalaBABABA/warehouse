import { message } from 'antd'
import ajax from './ajax'

export const listAll = () => ajax('/permissiontype/listAll')
export const DelPermissionType = (typeId) => ajax('/permissiontype/deltype', { typeId })
export const AddPermissionType = (type, resourceId) => ajax('/permissiontype/addtype', { type, resourceId }, 'POST')
export const AddResourceRequest = (values) => ajax('/permissiontype/addresource', { ...values }, 'POST')
export const DelResourceRequest = (resourceId) => ajax('/permissiontype/delresource', { resourceId })
export const AddUriRequest = (typeId, name) => ajax('/permissiontype/adduri', { typeId, name }, 'POST')
export const DelUriRequest = (id) => ajax('/permissiontype/deluri', { id })
export const UpdateResource = (resource) => ajax('/permissiontype/update/resource', resource, 'POST')
export const UpdateType = (type) => ajax('/permissiontype/update/type', type, 'POST')
export const UpdateUri = (uri) => ajax('/permissiontype/update/uri', uri, 'POST')