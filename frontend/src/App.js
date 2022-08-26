import React, {useEffect, useState} from 'react';

function App() {
  const [user, setUser] = useState({

  })

  useEffect(() => {
    getData().then((result) => setUser(result));
  }, []);

  async function getData() {
    const response = await fetch('/test/mybatis');
    return await response.json();
  }

  return (
      <div>
        {user}
      </div>
  );

}
export default App;