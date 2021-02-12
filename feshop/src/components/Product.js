import React from 'react'
import { Card } from 'react-bootstrap'
import { Link } from 'react-router-dom'
import { rupiahFormat } from '../utils/rupiahFormat'
import { config } from '../utils/constant'

const API_URL = config.url.API_URL

const Product = ({ product }) => {
  return (
    <Card className='my-3 p-3 rounded'>
      <Link to={`/product/${product.productId}`}>
        <Card.Img src={API_URL + product.attachment.image} variant='top' />
      </Link>

      <Card.Body>
        <Card.Title as='div'>
          <Link to={`/product/${product.productId}`}><strong>{product.name}</strong></Link>
        </Card.Title>

        {/* <Card.Text as='div'>
          <Rating
            value={product.rating}
            text={`${product.numReviews} reviews`}
          />
        </Card.Text> */}

        <Card.Text as='h5'>{rupiahFormat(product.price)}</Card.Text>
      </Card.Body>
    </Card>
  )
}

export default Product