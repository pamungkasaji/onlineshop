import React, { useEffect, useState, useRef } from 'react'
import { Form, Button, Container, Row, Col, Table, Image, Card } from 'react-bootstrap'
import { useDispatch, useSelector } from 'react-redux'
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
  shippingActionCreator
} from '../actions/shippingActions'

const ShippingPage = (props) => {
  const cart = useSelector((state) => state.cart)
  const { shippingAddress } = cart

  const [address, setAddress] = useState(shippingAddress.address)
  const [city, setCity] = useState(shippingAddress.city)
  const [postalCode, setPostalCode] = useState(shippingAddress.postalCode)
  const [country, setCountry] = useState(shippingAddress.country)

  const [couriers, setCouriers] = useState([])
  const cityRef = useRef(null)
  const provRef = useRef(null)
  const cityDestinationRef = useRef(null)
  const kurirRef = useRef(null)
  const beratRef = useRef(null)
  const { history, state, cityAllAction, provAllAction, cityDestination, shippingAction } = props
  const shippingCost = !state.btnDisabled && state.cost.flat(Infinity)[0]

  useEffect(() => {
    // cityAllAction()
    provAllAction()
  }, [])

  const dispatch = useDispatch()

  const submitHandler = (e) => {
    e.preventDefault()
    shippingAction('SHIPPING_SUCCESS', {
      // from: cityRef.current.value,
      from: '154', //jakarta timur code
      to: kabRef.current.value,
      weight: beratRef.current.value,
      courier: kurirRef.current.value,
      btnText: <i className="spinner-border spinner-border-sm" />,
      btnDisabled: true
    })
    // dispatch(saveShippingAddress({ address, city, postalCode, country }))
    history.push('/payment')
  }

  const onSelect = () => {
    // kabAllAction({ provId: provRef.current.value })
    setCouriers(['JNE', 'TIKI', 'POS'])
  }

  return (
    <FormContainer>
      <CheckoutSteps step1 step2 />
      <h1>Shipping</h1>
      <Form onSubmit={submitHandler}>
        <Form.Group controlId='address'>
          <Form.Label>Address</Form.Label>
          <Form.Control
            type='text'
            placeholder='Enter address'
            value={address}
            required
            onChange={(e) => setAddress(e.target.value)}
          ></Form.Control>
        </Form.Group>

        <Form.Group controlId='city'>
          <Form.Label>City</Form.Label>
          <Form.Control
            type='text'
            placeholder='Enter city'
            value={city}
            required
            onChange={(e) => setCity(e.target.value)}
          ></Form.Control>
        </Form.Group>

        <Form.Group controlId='postalCode'>
          <Form.Label>Postal Code</Form.Label>
          <Form.Control
            type='text'
            placeholder='Enter postal code'
            value={postalCode}
            required
            onChange={(e) => setPostalCode(e.target.value)}
          ></Form.Control>
        </Form.Group>

        <Form.Group controlId='country'>
          <Form.Label>Country</Form.Label>
          <Form.Control
            type='text'
            placeholder='Enter country'
            value={country}
            required
            onChange={(e) => setCountry(e.target.value)}
          ></Form.Control>
        </Form.Group>

        {/* <Form.Group>
          <Form.Label>Kota Asal</Form.Label>
          <Form.Control as="select" ref={cityRef} onChange={onSelect}>
            {state.kab.length < 1 && <option value="pilih kota">Pilih Kota Tujuan</option>}
            {state.city.map((c) => (
              <option key={c.city_id} value={c.city_id}>
                {c.city_name}
              </option>
            ))}
          </Form.Control>
        </Form.Group> */}

        <Form.Group>
          <Form.Label>Provinsi Tujuan</Form.Label>
          <Form.Control as="select" ref={provRef} onChange={onSelect}>
            {state.kab.length < 1 && <option value="pilih provinsi">Pilih Provinsi Tujuan</option>}
            {state.prov.map((p) => (
              <option key={p.province_id} value={p.province_id}>
                {p.province}
              </option>
            ))}
          </Form.Control>
        </Form.Group>

        <Form.Group>
          <Form.Label>Kabupaten Tujuan</Form.Label>
          <Form.Control as="select" ref={kabRef} onChange={onSelect}>
            <option value="pilih kabupaten">Pilih Kota/Kabupaten Tujuan</option>
            {state.cityInProv.map((k) => (
              <option key={k.city_id} value={k.city_id}>
                {k.city_name}
              </option>
            ))}
          </Form.Control>
        </Form.Group>

        <Form.Group>
          <Form.Label>Jasa Pengiriman</Form.Label>
          <Form.Control as="select" ref={kurirRef} onChange={onSelect}>
            {state.kab.length < 1 && <option value="pilih jasa pengiriman">Pilih Jasa Pengiriman</option>}
            {couriers.map((c) => (
              <option key={uuid()} value={c}>
                {c}
              </option>
            ))}
          </Form.Control>
        </Form.Group>

        <Form.Group>
          <Form.Label>Jumlah Berat (Kg)</Form.Label>
          <Form.Control
            type="number"
            ref={beratRef}
            placeholder="Masukan Jumlah Berat"
            onChange={onSelect}
            required
          />
        </Form.Group>

        <Form.Group>
          <button
            type="submit"
            className="btn btn-block rounded"
            disabled={state.btnDisabled}>
            {state.btnText}
          </button>
        </Form.Group>

        <Form.Group>
          <Card>
            <Card.Header className="p-3">
              <h5>Table Estimasi Pengiriman</h5>
            </Card.Header>
            <Card.Body>
              <Table bordered hover striped responsive="lg md sm">
                <thead>
                  <tr>
                    <th>Jenis Paket</th>
                    <th>Deskripsi</th>
                    <th>Estimasi Pengiriman</th>
                    <th>Ongkir (Rp)</th>
                  </tr>
                </thead>
                <tbody>
                  {shippingCost && (
                    <>
                      {shippingCost.costs.map((o) => (
                        <tr key={uuid()}>
                          <td>{o.service}</td>
                          <td>{o.description}</td>
                          {o.cost.map((v) => (
                            <>
                              <td>
                                {v.etd} {shippingCost.code !== 'pos' ? 'Hari' : ''}
                              </td>
                              <td>{rupiahFormat(v.value)}</td>
                            </>
                          ))}
                        </tr>
                      ))}
                    </>
                  )}
                </tbody>
              </Table>
            </Card.Body>
          </Card>
        </Form.Group>

        <Button type='submit' variant='primary'>
          Continue
        </Button>
      </Form>
    </FormContainer>
  )
}

const mapStateToProps = (state) => ({
  state: state.shipping
})

const mapDispatchToProps = (dispatch) => ({
  cityAllAction: () => dispatch(cityAllActionCreator()),
  provAllAction: () => dispatch(provAllActionCreator()),
  cityInProvActionCreator: (payload) => dispatch(cityInProvActionCreator({ ...payload })),
  shippingAction: (type, payload) => dispatch(shippingActionCreator(type, { ...payload }))
})

export default connect(mapStateToProps, mapDispatchToProps)(ShippingPage)