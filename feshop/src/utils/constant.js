const prod = {
    url: {
        API_URL: 'https://beonlineshop.herokuapp.com',
        // API_URL_USERS: 'https://beonlineshop.herokuapp.com/users'
   }
}

const dev = {
    url: {
        // API_URL: 'http://localhost:8080'
        API_URL: 'https://beonlineshop.herokuapp.com'
    }
}

export const config = process.env.NODE_ENV === 'development' ? dev : prod