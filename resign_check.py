# 安装命令
# pip install pandas
# pip install openpyxl
# pip install requests

import os
import pandas as pd
import openpyxl
import requests
import json
user = os.getlogin()

# 文件名
file_name = 'export_data_1720159474169'
# 输出文件名
output_file_name = 'wx_acount_number'

# 工号账号映射
number_account_dict = {}
# 在职工号
wx_number_list = []

# 查询外部接口
def query(employee_numbers):
    print('查询账号:'+employee_numbers)
    # 构造要发送的JSON数据
    data = {
        "extstaffId":employee_numbers
    }
    # 将JSON数据转换为字符串
    json_data = json.dumps(data)
    # 设置请求头
    headers = {
        "Content-Type": "application/json",
        "Authorization": token
    }
    # 发送POST请求
    response = requests.post(url=url, data=json_data, headers=headers)
    print('结果:'+response.text)
    data = json.loads(response.text)
    if len(data) == 0:
        return
    for row in data:
        if row['deleFlag'] == '0':
            wx_number_list.append(row['extstaffId'])

# 读Excel
def read_excel():
    # 读取Excel文件
    df = pd.read_excel(f'C:/Users/{user}/Downloads/{file_name}.xlsx')
    # 转换所有数据为字符串
    df = df.astype(str)
    # 获取第三行及其之后的数据，只包括第一列和第二列
    data = df.iloc[2:, 0:2].values.tolist()
    wx_numbers=[]
    for row in data:
        if not row[0].startswith("wx"):
            continue
        number_account_dict[row[0]] = row[1]
        # 满10个工号执行查询
        if len(wx_numbers) == 10:
            number_list_str = ','.join(wx_numbers)
            query(number_list_str)
            wx_numbers.clear()
        wx_numbers.append(row[0])
    if len(wx_numbers) != 0:
        number_list_str = ','.join(wx_numbers)
        query(number_list_str)


# 输出目标Excel
def write_excel():
    if len(wx_number_list) == 0:
        print('无数据差异')
        return
    data = []
    for account_number in wx_number_list:
        data.append([account_number , number_account_dict[account_number]])
    df = pd.DataFrame(data, columns=['employee_number', 'accoun_number'])
    output_file = f'C:/Users/{user}/Downloads/{output_file_name}.xlsx'
    df.to_excel(output_file, index=False)
    print('输出文件:'+output_file)
    

print('==start==')
read_excel()
write_excel()
print('==finish==')
