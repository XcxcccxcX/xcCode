<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>HELLO WORLD!!!</title>
</head>
<body>
HELLO ${name};
<br/>
遍历数据模型中的list学生信息(数据模型中的名称为 stus)   大于 gt
    <table>
        <tr>
            <td>序号</td>
            <td>姓名</td>
            <td>年龄</td>
            <td>金额</td>
            <td>生日</td>
        </tr>
        <#list stus as stu>
            <tr>
                <td>${stu_index+1}</td>
                <td<#if stu.name == '小明'> style="background-color: red" </#if>>${stu.name}</td>
                <td<#if (stu.age > 10)> style="background-color: blue" </#if>>${stu.age}</td>
                <td<#if stu.money gt 300> style="background-color: red" </#if>>${stu.money}</td>
                <td>${stu.birthday?date}</td>
                <td>${stu.birthday?string("YYYY年MM月dd日")}</td>
            </tr>
        </#list>
        <br/>
        学生个数: ${stus?size}
    </table>
<br/>

遍历数据模型中的map学生信息(map数据)
<br/>

<br/>
姓名: ${stuMap['stu1'].name}<br/>
年龄: ${stuMap['stu1'].age}<br/>
<br/>
</body>
</html>