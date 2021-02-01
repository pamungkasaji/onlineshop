import axios from 'axios'

export const shippingState = {
  city: [],
  prov: [],
  cityInProv: [],
  cost: [],
  cityId: '',
  provId: '',
  from: '',
  to: '',
  weight: '',
  courier: '',
  btnText: ' Cetak Data',
  btnDisabled: false
}

axios.interceptors.response.use((res) => {
  if (res.headers['content-type'] === 'application/json') {
    res.headers['accept'] = 'application/json'
    res.headers['content-type'] = 'application/json'
    res.config.headers['Accept'] = 'application/json'
    res.config.headers['Content-Type'] = 'application/json'
  }
  return res
})

export const provAllActionCreator = () => async (dispatch) => {
  const { data } = await axios.get('/api/shipping/province')

  dispatch({
    type: 'PROV_ALL',
    payload: data.rajaongkir.results
  })
}

export const cityAllActionCreator = () => async (dispatch) => {
  const { data } = await axios.get('/api/shipping/city')

  dispatch({
    type: 'CITY_ALL',
    payload: data.rajaongkir.results
  })
}

export const cityInProvActionCreator = (provId) => async (dispatch) => {
  const { data } = await axios.get(`/api/shipping/city?province=${provId}`)

  dispatch({
    type: 'CITY_IN_PROV',
    payload: data.rajaongkir.results
  })
}

export const shippingActionCreator = (type, payload) => async (dispatch) => {
  const { from, to, weight, courier } = payload
  const { data } = await axios.post('/api/shipping/cost', { from, to, weight, courier: courier.toLowerCase() })

  dispatch({
    type: type,
    payload: {
      cost: data.rajaongkir.results,
      btnText: payload.btnText,
      btnDisabled: payload.btnDisabled
    }
  })

  setTimeout(() => {
    dispatch({
      type: 'SHIPPING_CLEANUP',
      payload: { ...shippingState }
    })
  }, 3000)
}
