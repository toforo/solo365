<#--

    Solo - A small and beautiful blogging system written in Java.
    Copyright (c) 2010-present, b3log.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

-->
<#include "macro-common_page.ftl">

<@commonPage "${welcomeToSoloLabel}!">
<h2>
    <span>${welcomeToSoloLabel}</span>
    <a target="_blank" href="https://solo.b3log.org">
        <span class="error">&nbsp;Solo</span>
    </a>
</h2>

<div id="login">
    <form class="form" method="POST" action="${servePath}/login">
        <label for="userName">用户名</label>
        <input type="text" id="userName" name="userName" class="login__text"/>
        <label for="userPassword">密码</label>
        <input type="password" id="userPassword" name="userPassword" class="login__text"/>
        <div style="text-align: fn__right">
         <#if msg??>
            <span class="error">${msg}</span>
         </#if>
        <button type="submit">${loginLabel}</button>
    </div>
    </form>
</div>
</@commonPage>
