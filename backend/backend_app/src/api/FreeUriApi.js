import { message } from 'antd'
import ajax from './ajax'

export const listAll = () => ajax('/free_uri/list')
export const del = (list) => ajax('/free_uri/del', list, 'POST')
export const add = (uri) => ajax('/free_uri/add', { uri })
export const FreeByResourceId = (resourceId) => ajax('/free_uri/byresource', { resourceId })
export const FreeByPermissionTypeId = (typeId) => ajax('/free_uri/bytype', { typeId })
export const FreeByUriId = (uriId) => ajax('/free_uri/byuri', { uriId })
export const updateUri = (uri) => ajax('/free_uri/update', uri, 'POST')
