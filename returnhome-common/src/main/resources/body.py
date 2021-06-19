# coding=utf-8
import json
import base64
from tencentcloud.common import credential
from tencentcloud.common.profile.client_profile import ClientProfile
from tencentcloud.common.profile.http_profile import HttpProfile
from tencentcloud.common.exception.tencent_cloud_sdk_exception import TencentCloudSDKException
from tencentcloud.bda.v20200324 import bda_client, models

try: 
    cred = credential.Credential("AKIDC9rMZg2UEjyyiEgpjMjvssb5JJ3Wpn0x", "rSo79NRQ8am7HAkmY3HiOTIrBb8PhllG") 
    httpProfile = HttpProfile()
    httpProfile.endpoint = "bda.tencentcloudapi.com"

    clientProfile = ClientProfile()
    clientProfile.httpProfile = httpProfile
    client = bda_client.BdaClient(cred, "ap-shanghai", clientProfile) 
except TencentCloudSDKException as err: 
    print(err)
    
def search(image_name, group_id, user_id):
    groupIdList = str(group_id) # 指定照片组
    try:
        req = models.SearchTraceRequest()
        params = {
            "Trace": {
                # "Images": [ image_name ],   #base64Image
                "Urls": [ image_name ]   #url和images任选其一
            #    "BodyRects": [
            #        {
            #            "X": 0,
            #            "Y": 0,
            #            "Width": 0,
            #            "Height": 0
            #        }
            #    ]
            },
            "GroupId": groupIdList,
            # "MaxPersonNum": 3,
            # "TraceMatchThreshold": 0
        }
        req.from_json_string(json.dumps(params))

        resp = client.SearchTrace(req)
        return resp

    except TencentCloudSDKException as err:
        print(err)

def enrollGroup(group_id):
    try:
        req = models.CreateGroupRequest()
        params = {
            "GroupName": str(group_id),
            "GroupId": str(group_id)
        }
        req.from_json_string(json.dumps(params))

        resp = client.CreateGroup(req)
        return resp

    except TencentCloudSDKException as err: 
        print(err)

def enrollUser(image_name, group_id, user_id):
    groupId = str(group_id) # 指定照片组名字
    userId = str(user_id)
    try:
        req = models.CreatePersonRequest()
        params = {
            "Trace": {
                # "Images": [ image_name ]
                "Urls": [ image_name ]   #url和images任选其一
            },
            "GroupId": groupId,
            "PersonName": userId,
            "PersonId": userId
        }
        req.from_json_string(json.dumps(params))

        resp = client.CreatePerson(req)
        return resp

    except TencentCloudSDKException as err:
        print(err)
        
def updateBody(image_name, group_id, user_id):
    groupId = str(group_id) # 指定照片组名字
    userId = str(user_id)
    try:
        req = models.CreateTraceRequest()
        params = {
            "Trace": {
                # "Images": [ image_name ]
                "Urls": [ image_name ]   #url和images任选其一
            },
            "PersonId": userId
        }
        req.from_json_string(json.dumps(params))

        resp = client.CreateTrace(req)
        return resp

    except TencentCloudSDKException as err:
        print(err)
        
def deleteGroup(group_id):
    try:
        req = models.DeleteGroupRequest()
        params = {
            "GroupId": str(group_id)
        }
        req.from_json_string(json.dumps(params))

        resp = client.DeleteGroup(req)
        return resp

    except TencentCloudSDKException as err:
        print(err)
        
def deleteUser(group_id, user_id):
    try:
        req = models.DeletePersonRequest()
        params = {
            "PersonId": str(user_id)
        }
        req.from_json_string(json.dumps(params))

        resp = client.DeletePerson(req)
        return resp

    except TencentCloudSDKException as err:
        print(err)
        
if __name__ == '__main__':
    from sys import argv
    
    func = argv[1]
    image_name = argv[2]
    group_id = argv[3]
    user_id = argv[4]
    
    if func in ['search', 'enrollUser', 'updateBody']:
        print(eval(func)(image_name, group_id, user_id))
    elif func == 'deleteUser':
        print(eval(func)(group_id, user_id))
    elif func in ['enrollGroup', 'deleteGroup']:
        print(eval(func)(group_id))