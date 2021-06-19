import base64
from aip import AipFace

""" 在导入包时新建一个AipFace连接 """
APP_ID = '23600168'
API_KEY = 'Gktn4NljPn4eRbG0n74oqDNR'
SECRET_KEY = '85tc3fWWrRyPManSClb8ALYk7U2vPppP'

client = AipFace(APP_ID, API_KEY, SECRET_KEY)
#print("----已新建AipFace----")

def search(image_name, group_id, user_id):
    """
    人脸搜索
    输入：图片（兼容基本格式）， 用户所在用户组， 对比的用户id
    输出：json格式的结果
    """

    imageType = "URL"

    groupIdList = str(group_id) # 指定照片组
    
    """ 可选参数 """
    options = {}
    # options["max_face_num"] = 2
    # options["quality_control"] = "NORMAL" # 对图片质量有一定的但较低的要求（在测试时取消这一设定）
    # options["match_threshold"] = 80 # 阈值
    # options["face_type"] = "LIVE" # 表示输入为手机拍摄的生活照
    # options["user_id"] = str(user_id) # 指定对应的用户
    # options["liveness_control"] = "LOW" # 较低的活体要求，防止用非活体照片进行的攻击

    """ 调用人脸搜索 """
    return client.multiSearch(image_name, imageType, groupIdList, options)
    
def detect(image_name):
    imageType = "URL"
    
    options = {}
    options["face_field"] = "age,gender"
    options["max_face_num"] = 1

    """ 调用人脸检测 """
    return client.detect(image_name, imageType, options)
    
def enrollUser(image_name, group_id, user_id, user_info = None):
    """
    人脸注册
    输入：图片（兼容基本格式）， 用户所在用户组， 对比的用户id， 可选的用户信息
    输出：json格式的结果
    """
    """ 转换为base64格式的图片 """
    imageType = "URL"

    groupId = str(group_id) # 指定照片组名字
    userId = str(user_id)
    
    """ 可选参数 """
    options = {}
    # options["user_info"] = user_info
    # options["quality_control"] = "NORMAL"
    # options["liveness_control"] = "LOW"
    # options["action_type"] = "REPLACE" # 默认append

    """ 调用人脸注册 """
    return client.addUser(image_name, imageType, groupId, userId, options)
    
def updateFace(image_name, group_id, user_id, user_info = None, action_type = "APPEND"):
    """
    人脸更新
    输入：图片（兼容基本格式）， 用户所在用户组， 对比的用户id， 可选的用户信息(， 可选的更新方式)
    输出：json格式的结果
    """
    """ 转换为base64格式的图片 """
    imageType = "URL"

    groupId = str(group_id) # 指定照片组名字
    userId = str(user_id)
    
    """ 可选参数 """
    options = {}
    # options["user_info"] = user_info
    # options["quality_control"] = "NORMAL"
    # options["liveness_control"] = "LOW"
    # options["action_type"] = action_type

    """ 调用人脸更新 """
    return client.addUser(image_name, imageType, groupId, userId, options) # 此处官方文档中的说法和实际不同，所以用add暂替
    
def deleteFace(group_id, user_id, face_token):
    """
    人脸删除
    输入：图片（兼容基本格式）， 用户所在用户组， 对比的用户id， 可选的用户信息， 可选的更新方式
    输出：json格式的结果
    """
    """ 转换为base64格式的图片 """
    userId = str(user_id)
    
    groupId = str(group_id)

    faceToken = face_token

    """ 人脸删除 """
    return client.faceDelete(userId, groupId, faceToken)
    
def deleteUser(group_id, user_id):
    """
    用户删除
    输入：用户所在用户组， 对比的用户id
    输出：json格式的结果
    """
    return client.deleteUser(str(group_id), str(user_id))
    
def enrollGroup(group_id):
    """
    用户组注册
    输入：用户组
    输出：json格式的结果
    """
    return client.groupAdd(str(group_id))
    
def deleteGroup(group_id):
    """
    用户组删除
    输入：所在用户组
    输出：json格式的结果
    """
    return client.groupDelete(str(group_id))
    
if __name__ == '__main__':
    from sys import argv
    
    func = argv[1]
    image_name = argv[2]
    group_id = argv[3]
    user_id = argv[4]
    
    if func in ['search', 'enrollUser', 'updateFace']:
        print(eval(func)(image_name, group_id, user_id))
    elif func == 'detect':
        print(eval(func)(image_name))
    elif func == 'deleteUser':
        print(eval(func)(group_id, user_id))
    elif func in ['enrollGroup', 'deleteGroup']:
        print(eval(func)(group_id))