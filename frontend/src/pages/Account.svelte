<script>
    import {user} from "../store";
</script>


<h1 class="mt-3">Account Details</h1>
<div class="row mb-3">
    <div class="col">
        <img src="{$user.picture}" alt="icon"/>
    </div>
</div>
<div class="row mb-3">
    <div class="col">
        <table>
            <tr>
                <td><b>Name:</b></td>
                <td>{$user.name}</td>
            </tr>
            <tr>
                <td><b>Nickname:</b></td>
                <td>{$user.nickname}</td>
            </tr>
            <tr>
                <td><b>Firstname:</b></td>
                <td>{$user.given_name}</td>
            </tr>
            <tr>
                <td><b>Lastname:</b></td>
                <td>{$user.family_name}</td>
            </tr>
            <tr>
                <td><b>Email:</b></td>
                <td>{$user.email}</td>
            </tr>

            {#if $user.user_roles && $user.user_roles.length > 0}
                <tr>
                    <td><b>Roles:</b></td>
                    <td>{$user.user_roles}</td>
                </tr>
            {/if}
        </table>
    </div>
</div>
