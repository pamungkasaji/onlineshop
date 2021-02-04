import React, { useEffect, useState, useRef } from 'react'
import { Form, Button, Col, Row, ListGroup, Table, Image, Card } from 'react-bootstrap'
import { useDispatch, useSelector } from 'react-redux'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import uuid from 'react-uuid'
import FormContainer from '../components/FormContainer'
import CheckoutSteps from '../components/CheckoutSteps'
import { saveShippingAddress } from '../actions/cartActions'
import { rupiahFormat } from '../utils/rupiahFormat'
import {
  cityAllActionCreator,
  provAllActionCreator,
  cityInProvActionCreator,
  minShippingActionCreator,
  allShippingActionCreator
} from '../actions/shippingActions'

const ShippingPage = (props) => {
  const cart = useSelector((state) => state.cart)
  const { shippingAddress } = cart

  // const [address, setAddress] = useState(shippingAddress.address)
  // const [city, setCity] = useState(shippingAddress.city)
  // const [postalCode, setPostalCode] = useState(shippingAddress.postalCode)
  // const [country, setCountry] = useState(shippingAddress.country)
  const [couriers, setCouriers] = useState([])
  const provRef = useRef(null)
  const cityToRef = useRef(null)
  const kurirRef = useRef(null)

  const [address, setAddress] = useState('')
  const [selectedProv, setSelectedProv] = useState('')
  const [selectedCity, setSelectedCity] = useState('')

  const { history, state, cityAllAction, provAllAction, cityInProvAction, minShippingAction, allShippingAction } = props
  const shippingCost = !state.btnDisabled && state.cost.flat(Infinity)[0]

  useEffect(() => {
    cityAllAction()
    provAllAction()
  }, [])

  const dispatch = useDispatch()

  const submitHandler = (e) => {
    e.preventDefault()
    dispatch(saveShippingAddress({
      fullAddress: address + ', ' + selectedCity.type + ' ' + selectedCity.city_name + ' ' + ' Provinsi ' + selectedProv,
      shippingPrice: state.minCost.value,
      estimated: state.minCost.etd
    }))
    history.push('/placeorder')
  }

  const onSelectProvince = () => {
    console.log('provRef: ', provRef.current.value)
    cityInProvAction(provRef.current.value)
    setCouriers(['JNE', 'TIKI', 'POS'])
    setSelectedProv(state.prov[provRef.current.value - 1].province)
    setSelectedCity('')
  }

  const onSelectCity = () => {
    console.log('cityToRef: ', cityToRef.current.value)
    minShippingAction({
      destination: cityToRef.current.value,
      weight: 1000,
      courier: kurirRef.current.value,
    })
    setSelectedCity(state.city[cityToRef.current.value - 1])
  }

  // const onAddressChange = () => {
  //   setFullAddress(address + ', ' + selectedCity.type + selectedCity.city_name + selectedProv)
  // }

  return (
    <FormContainer>
      <CheckoutSteps step1 step2 />
      <h3>Alamat Pengiriman</h3>
      <Form onSubmit={submitHandler}>

        <Form.Group>
          <Form.Label>Provinsi Tujuan</Form.Label>
          <Form.Control as="select" ref={provRef} onChange={onSelectProvince} required>
            {state.cityInProv.length < 1 && <option value="pilih provinsi">Pilih Provinsi Tujuan</option>}
            {state.prov.map((p) => (
              <option key={p.province_id} value={p.province_id}>
                {p.province}
              </option>
            ))}
          </Form.Control>
        </Form.Group>

        <Form.Group>
          <Form.Label>Kota/Kabupaten Tujuan</Form.Label>
          <Form.Control as="select" ref={cityToRef} onChange={onSelectCity} required>
            <option value="pilih kabupaten">Pilih Kota/Kabupaten Tujuan</option>
            {state.cityInProv.map((k) => (
              <option key={k.city_id} value={k.city_id}>
                {k.city_name}
              </option>
            ))}
          </Form.Control>
        </Form.Group>

        <Form.Group controlId='address'>
          <Form.Label>Detail Alamat</Form.Label>
          <Form.Control
            type='text'
            placeholder='Nama Jalan, RT RW, Kelurahan, Kecamatan'
            value={address}
            required
            onChange={(e) => setAddress(e.target.value)}
          ></Form.Control>
        </Form.Group>

        <Form.Group>
          <Form.Label>Jasa Pengiriman</Form.Label>
          <Form.Control as="select" ref={kurirRef} required>
            {state.cityInProv.length < 1 && <option value="pilih jasa pengiriman">Pilih Jasa Pengiriman</option>}
            {couriers.map((c) => (
              <option key={uuid()} value={c}>
                {c}
              </option>
            ))}
          </Form.Control>
        </Form.Group>

        {/* <Form.Group>
          <Form.Label>Jumlah Berat (Kg)</Form.Label>
          <Form.Control
            type="number"
            ref={beratRef}
            placeholder="Masukan Jumlah Berat"
            required
          />
        </Form.Group> */}

        {/* <p>Alamat Lengkap: {address}, {selectedCity.type} {selectedCity.city_name}, {selectedProv} </p>
        <h6>Biaya pengiriman: {rupiahFormat(state.minCost.value)}</h6>
        <h6>Waktu pengiriman: {state.minCost.etd}</h6>

        <Button
          type='submit'
          variant='primary'>
          Continue
        </Button> */}


      <ListGroup variant='flush'>
        <ListGroup.Item>
          <Row>
            <Col md={4}>Alamat Lengkap:</Col>
            <Col>
              {address}, {selectedCity.type} {selectedCity.city_name}, {selectedProv}
            </Col>
          </Row>
        </ListGroup.Item>

        <ListGroup.Item>
          <Row>
            <Col md={4}>Biaya Pengiriman:</Col>
            <Col>
              {rupiahFormat(state.minCost.value)}
            </Col>
          </Row>
        </ListGroup.Item>

        <ListGroup.Item>
          <Row>
            <Col md={4}>Lama Pengiriman:</Col>
            <Col>
              {state.minCost.etd} hari
            </Col>
          </Row>
        </ListGroup.Item>

        <Button
          type='submit'
          variant='primary'>
          Continue
        </Button>
      </ListGroup>
      </Form>
    </FormContainer>
  )
}

ShippingPage.propTypes = {
  history: PropTypes.object,
  state: PropTypes.object,
  cityAllAction: PropTypes.func,
  provAllAction: PropTypes.func,
  cityInProvAction: PropTypes.func,
  minShippingAction: PropTypes.func,
  allShippingAction: PropTypes.func
}

const mapStateToProps = (state) => ({
  state: state.shipping
})

const mapDispatchToProps = (dispatch) => ({
  cityAllAction: () => dispatch(cityAllActionCreator()),
  provAllAction: () => dispatch(provAllActionCreator()),
  cityInProvAction: (provId) => dispatch(cityInProvActionCreator(provId)),
  minShippingAction: (shippingData) => dispatch(minShippingActionCreator(shippingData)),
  allShippingAction: (type, payload) => dispatch(allShippingActionCreator(type, { ...payload }))
})

export default connect(mapStateToProps, mapDispatchToProps)(ShippingPage)