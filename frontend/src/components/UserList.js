import React from 'react';

const UserList = ({ users }) => {
    return (
        <div>
            {users.map(user => {
                return (<div key={user.id}>
                    {user.u}
                </div>)
            })}
        </div>
    );
};

export default UserList;